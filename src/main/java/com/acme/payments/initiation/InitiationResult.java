package com.acme.payments.initiation;

public record InitiationResult(
        SubmissionId submissionId,
        PaymentId paymentId
) {
}
