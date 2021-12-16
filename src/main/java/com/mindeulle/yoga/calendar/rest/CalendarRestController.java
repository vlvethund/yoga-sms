package com.mindeulle.yoga.calendar.rest;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/mindeulle/yoga/calendar")
public class CalendarRestController {

    private final Logger logger;

    public CalendarRestController() {
        logger = LoggerFactory.getLogger("logger");
    }

    @PostMapping("/test")
    public void execute(@RequestBody String something) {
        logger.info(something);
    }

}
