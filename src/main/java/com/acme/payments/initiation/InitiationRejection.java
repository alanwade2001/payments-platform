package com.acme.payments.initiation;

public record InitiationRejection(
        InitiationRejectionCode code,
        String message
) {
}
