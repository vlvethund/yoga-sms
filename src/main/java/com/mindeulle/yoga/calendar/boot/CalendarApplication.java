package com.mindeulle.yoga.calendar.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.mindeulle.yoga.calendar"})
public class CalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }

}
