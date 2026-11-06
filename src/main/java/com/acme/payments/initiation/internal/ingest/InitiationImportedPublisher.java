package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.InitiationId;
import com.acme.payments.initiation.InitiationImported;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitiationImportedPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(InitiationId initiationId) {
        applicationEventPublisher.publishEvent(new InitiationImported(initiationId));
    }
}
