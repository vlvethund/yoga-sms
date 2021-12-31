package com.mindeulle.yoga.calendar.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mindeulle/yoga/calendar")
public class CalendarRestController {
    //
    @CrossOrigin
    @GetMapping("/test")
    public String execute() {
        //
        return "hello";
    }
}
