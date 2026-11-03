package com.acme.payments.engine;

import com.acme.payments.initiation.PaymentId;
import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.Iban;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;


public record CreditTransfer(
        String externalReference,
        Iban debtorIban,
        Iban creditorIban,
        String endToEndId,
        BigDecimal amount,
        Currency currency,
        LocalDate executionDate
) {
}
