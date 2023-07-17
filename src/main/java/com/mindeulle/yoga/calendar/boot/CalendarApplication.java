package com.mindeulle.yoga.calendar.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.mindeulle.yoga.calendar"})
public class CalendarApplication {

    public static void main(String[] args) {
        //
        SpringApplication app = new SpringApplication(CalendarApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
        System.exit(0);
    }

}
