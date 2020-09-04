package ru.grasshopper.ws.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SleepingConfig {
    @Value("${sleepingTimeSec:0}")
    private Long sleepingTimeSec;

    public Long getSleepingTimeMilli() {
        return sleepingTimeSec * 1000;
    }
}