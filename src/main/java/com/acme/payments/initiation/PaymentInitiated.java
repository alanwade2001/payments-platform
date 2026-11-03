package com.acme.payments.initiation;

import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.Iban;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public record PaymentInitiated(
        SubmissionId submissionId,
        PaymentId paymentId,
        AccountId debtorAccountId,
        Iban debtorIban,
        Iban creditorIban,
        String endToEndId,
        BigDecimal amount,
        Currency currency,
        LocalDate requestedExecutionDate
) {
}