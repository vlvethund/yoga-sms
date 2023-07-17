package com.mindeulle.yoga.calendar.runner;

import com.mindeulle.yoga.calendar.logic.SchedulerLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@RequiredArgsConstructor
public class MessageRunner implements ApplicationRunner {

    private final SchedulerLogic schedulerLogic;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        schedulerLogic.proceed();
    }
}
