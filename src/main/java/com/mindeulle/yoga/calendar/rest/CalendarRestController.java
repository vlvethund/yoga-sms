package com.mindeulle.yoga.calendar.rest;

import com.mindeulle.yoga.calendar.logic.SchedulerLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mindeulle/yoga/calendar")
public class CalendarRestController {
    //

    private final SchedulerLogic schedulerLogic;

    @CrossOrigin
    @GetMapping("/awake")
    public String executeAwaken() {
        //
        return "hello";
    }

    @CrossOrigin
    @GetMapping("/test")
    public String executeTest() throws GeneralSecurityException, IOException {
        //
        schedulerLogic.proceedTest();
        return "hello";
    }




}
