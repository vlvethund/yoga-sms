package com.mindeulle.yoga.calendar.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    //
    private String to; // Optional: -를 제외한 숫자만 입력 가능
    private String subject; // Optional: LMS, MMS에서만 사용 가능
    private String content; // Optional: SMS: 최대 80byte. LMS, MMS: 최대 2000byte


}
