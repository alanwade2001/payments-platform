package com.acme.payments.initiation;

import java.util.List;

public sealed interface InitiationOutcome {

    record Accepted(
            SubmissionId submissionId,
            PaymentId paymentId
    ) implements InitiationOutcome {}

    record Rejected(
            List<InitiationRejection> reasons
    ) implements InitiationOutcome {

        public Rejected(InitiationRejection reason) {
            this(List.of(reason));
        }
    }
}
