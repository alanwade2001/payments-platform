package com.acme.payments.engine;

public record InstructionMetadata(
        TransactionId transactionId,
        SourceReference sourceReference,
        SchemeId schemeId
) {
}
