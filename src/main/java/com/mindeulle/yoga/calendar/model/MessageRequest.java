package com.mindeulle.yoga.calendar.model;

import com.mindeulle.yoga.calendar.model.vo.File;
import com.mindeulle.yoga.calendar.model.vo.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessageRequest {
    //
    private String type; // Madantory: SMS, LMS, MMS (소문자 가능)
    private String contentType; // Optional: COMM: 일반메시지 AD: 광고메시지 (default: COMM)
    private String countryCode; // Optional: SENS에서 제공하는 국가`로의 발송만 가능 (default: 82)
    private String from; // Mandatory: 사전 등록된 발신번호만 사용 가능
    private String subject; // Optional: LMS, MMS에서만 사용 가능
    private String content; // Mandatory: SMS: 최대 80byte LMS, MMS: 최대 2000byte
    private List<Message> messages; // Mandatory: 최대 1,000개
    private List<File> files; // Optional:
    private String reserveTime; // Optional: 메시지 발송 예약 일시 (yyyy-MM-dd HH:mm)
    private String reserveTimeZone; // Optional: 예약 일시 타임존 (기본: Asia/Seoul)
    private String scheduleCode; // Optional: 등록하려는 스케줄 코드


    public MessageRequest() {
        this.messages = new ArrayList<>();
        this.files = new ArrayList<>();
    }
}

