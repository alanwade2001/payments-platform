package com.acme.payments.initiation.internal.dispatch;

import com.acme.payments.engine.InstructedTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InstructedTransactionPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(InstructedTransaction instructedTransaction) {
        log.info("Publishing instructed transaction: {}", instructedTransaction);
        applicationEventPublisher.publishEvent(instructedTransaction);
    }
}
