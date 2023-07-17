package com.mindeulle.yoga.calendar.task;

import com.mindeulle.yoga.calendar.logic.SchedulerLogic;
import com.mindeulle.yoga.calendar.runner.MessageRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name= {"task"}, havingValue = "message")
public class MessageTaskConfig {
    //
    private final SchedulerLogic schedulerLogic;

    @Bean
    ApplicationRunner getRunner() {
        return new MessageRunner(schedulerLogic);
    }
}
