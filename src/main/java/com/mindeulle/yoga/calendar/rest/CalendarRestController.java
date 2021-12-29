package com.mindeulle.yoga.calendar.rest;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/mindeulle/yoga/calendar")
public class CalendarRestController {
    //
    private final Logger logger;

    public CalendarRestController() {
        //
        logger = LoggerFactory.getLogger("logger");
    }

    @CrossOrigin
    @PostMapping  ("/test")
    public String execute(@RequestBody String something) {
        //
        logger.info(something);
        return "hello";
    }

}
