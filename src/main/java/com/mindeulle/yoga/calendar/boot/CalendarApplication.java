package com.mindeulle.yoga.calendar.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.mindeulle.yoga.calendar"})
public class CalendarApplication {

    public static void main(String[] args) {
        //
        System.out.println("test");
        SpringApplication.run(CalendarApplication.class, args);
        log.info("Boot Started");
    }

}
