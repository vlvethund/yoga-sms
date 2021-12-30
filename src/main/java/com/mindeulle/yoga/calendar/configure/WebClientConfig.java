package com.mindeulle.yoga.calendar.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
                .build();

        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient
//                                        .create()
//                                        .secure(
//                                                ThrowingConsumer.unchecked(
//                                                        sslContextSpec -> sslContextSpec.sslContext(
//                                                                SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
//                                                        )
//                                                )
//                                        )
//                                        .tcpConfiguration(
//                                                client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
//                                                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
//                                                                .addHandlerLast(new WriteTimeoutHandler(180))
//                                                        )
//                                        )
//                        )
//                )
//                .exchangeStrategies(exchangeStrategies)
                .baseUrl("https://sens.apigw.ntruss.com/sms")
                .build();

    }

}
