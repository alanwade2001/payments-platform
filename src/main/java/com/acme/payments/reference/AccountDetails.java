package com.acme.payments.reference;

import com.acme.payments.model.Iban;

import java.util.Currency;

public record AccountDetails(
        AccountId accountId,
        CustomerId customerId,
        Iban iban,
        Currency currency,
        AccountStatus status
) {
}
