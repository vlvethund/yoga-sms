package com.mindeulle.yoga.calendar.model;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRespond {
    private String requestId;
    private DateTime requestTime;
    private String statusCode;
    private String statusName;
}
