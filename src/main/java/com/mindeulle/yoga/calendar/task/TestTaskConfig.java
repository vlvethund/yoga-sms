package com.mindeulle.yoga.calendar.task;

import com.mindeulle.yoga.calendar.logic.SchedulerLogic;
import com.mindeulle.yoga.calendar.runner.TestRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name= {"task"}, havingValue = "test")
public class TestTaskConfig {
    //
    private final SchedulerLogic schedulerLogic;

    @Bean
    ApplicationRunner getRunner() {
        return new TestRunner(schedulerLogic);
    }
}
