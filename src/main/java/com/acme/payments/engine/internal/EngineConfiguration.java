package com.acme.payments.engine.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class EngineConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
