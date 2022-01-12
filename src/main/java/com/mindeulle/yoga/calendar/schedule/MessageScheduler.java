package com.mindeulle.yoga.calendar.schedule;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.gson.*;
import com.mindeulle.yoga.calendar.model.MessageRequest;
import com.mindeulle.yoga.calendar.model.MessageRespond;
import com.mindeulle.yoga.calendar.model.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MessageScheduler {

    public MessageScheduler(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    private static final String APPLICATION_NAME = "mindeulle-calendar";
    private static final String GOOGLE_CALENDAR_ID = "1tq87lipcdr56pfavrevq517r8@group.calendar.google.com";
    private static final String NC_SERVICE_ID = "ncp:sms:kr:277938056372:yoga-sms";
    private static final String NC_ACCESS_KEY = "a5imXcry3TDgoS3vD6rD";
    private static final String NC_SECRET_KEY = "h7AP1GsKucfIkOFQao3BjyotCJWyEMdXpxj9bDET";
    private static final String OWNER_MOBILE = "01020252785";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        //
        log.info("Scheduling Process: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        InputStream in = MessageScheduler.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void proceed() throws IOException, GeneralSecurityException {
        //
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        LocalDateTime nextDayStart = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime nextDayEnd = nextDayStart.plusDays(1).minusSeconds(1);
        long nextDayStartTimeMillis = nextDayStart.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        long nextDayEndTimeMillis = nextDayEnd.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        DateTime nextDayStartDateTime = new DateTime(nextDayStartTimeMillis);
        DateTime nextDayEndDateTime = new DateTime(nextDayEndTimeMillis);

        Events events = service.events().list(GOOGLE_CALENDAR_ID)
                .setTimeMin(nextDayStartDateTime)
                .setTimeMax(nextDayEndDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        Map<String, JsonArray> attendeesInfo = new HashMap<>();
        if (items.isEmpty()) {
            log.info("No Upcoming Events Found");
            return;
        } else {
            for (Event event : items) {
                try {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    String startTimeString = Instant.ofEpochMilli(start.getValue()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime().toLocalTime().toString();

                    JsonObject jsonObject = new Gson().fromJson(event.getDescription(), JsonObject.class);
                    attendeesInfo.put(startTimeString, jsonObject.getAsJsonArray("attendees"));
                } catch (JsonSyntaxException e) {
                    log.info("Description Of The Event Cannot Be Formatted As Json. Event Id: " + event.getId());
                }
            }
        }

        MessageRequest messageRequest = new MessageRequest();
        StringBuilder ownerMessage = new StringBuilder("내일 예약 현황입니다 \n");

        messageRequest.setType("MMS");
        messageRequest.setFrom(OWNER_MOBILE);
        messageRequest.setSubject("수업 예약 알림");
        messageRequest.setContent("안녕하세요, 민들레요가 입니다." +
                " 내일 수업이 예약되어있습니다." +
                " 당일 취소는 횟수 차감이니" +
                " 전날 취소해주세요");

        for (String key : attendeesInfo.keySet()) {
            ownerMessage.append("[").append(key).append("] ");
            JsonArray jsonElements = attendeesInfo.get(key);
            for (JsonElement element : jsonElements) {
                String name = element.getAsJsonObject().get("name").getAsString();
                String mobile = element.getAsJsonObject().get("mobile").getAsString();

                Message message = new Message();
                message.setTo(mobile);
                message.setContent(String.format("안녕하세요 %s님, 민들레요가입니다." +
                        " 내일 %s 타임 수업이 예약되어있습니다." +
                        " 당일 취소는 횟수 차감이니" +
                        " 전날 취소해주세요", name, key));
                messageRequest.getMessages().add(message);
                ownerMessage.append(name).append(" ");
            }
            ownerMessage.append("// \n");
        }

        Message toOwner = new Message();
        toOwner.setSubject("수업 예약 현황");
        toOwner.setTo(OWNER_MOBILE);
        toOwner.setContent(ownerMessage.toString());
        messageRequest.getMessages().add(toOwner);

        long currentTime = System.currentTimeMillis();
        String signature = makeSignature(NC_SERVICE_ID, NC_ACCESS_KEY, NC_SECRET_KEY, currentTime);

        if (messageRequest.getMessages().size() == 0) {
            log.info("No Messages To Send. Current Message Size: " + messageRequest.getMessages().size());
            return;
        }

        Mono<MessageRespond> messageRespondMono = webClient.post()
                .uri(String.format("/v2/services/%s/messages", NC_SERVICE_ID))
                .headers(headers -> {
                    headers.add("Content-Type", "application/json; charset=utf-8");
                    headers.add("x-ncp-apigw-signature-v2", signature);
                    headers.add("x-ncp-apigw-timestamp", String.valueOf(currentTime));
                    headers.add("x-ncp-iam-access-key", NC_ACCESS_KEY);
                })
                .body(Mono.just(messageRequest), MessageRequest.class)
                .retrieve()
                .bodyToMono(MessageRespond.class);
        messageRespondMono.block();
        log.info("Sending Messages Success. Today Message Size: " + messageRequest.getMessages().size());
    }

    public String makeSignature(String serviceId, String accessKey, String secretKey, Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + serviceId + "/messages";
        String timestamp = time.toString();

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(rawHmac);
    }

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 20 * * ?")
    public void sendSmsEveryDay() throws IOException, GeneralSecurityException {
        proceed();
    }
}
