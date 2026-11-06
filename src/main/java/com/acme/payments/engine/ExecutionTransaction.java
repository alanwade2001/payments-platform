package com.acme.payments.engine;

import iso.std.iso._20022.tech.xsd.pacs_008_001.CreditTransferTransaction73;

import java.time.LocalDate;

public record ExecutionTransaction(
        ExecutionTransactionId id,
        InstructedTransactionId instructedTransactionId,
        TransactionId transactionId,
        SchemeId scheme,
        LocalDate executionDate,
        CreditTransferTransaction73 transaction,
        ExecutionStatus status
) {
}
