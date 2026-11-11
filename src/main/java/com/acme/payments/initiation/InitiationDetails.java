package com.acme.payments.initiation;

import java.time.Instant;
import java.util.List;

public record InitiationDetails(
        InitiationId id,
        String messageId,
        InitiationStatus status,
        Instant receivedAt) {
}
