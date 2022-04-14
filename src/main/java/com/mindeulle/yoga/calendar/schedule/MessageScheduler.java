package com.mindeulle.yoga.calendar.schedule;

import com.mindeulle.yoga.calendar.logic.SchedulerLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final SchedulerLogic schedulerLogic;

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 20 * * ?")
    public void sendSmsEveryDay() throws IOException, GeneralSecurityException {
        schedulerLogic.proceed();
    }
}
