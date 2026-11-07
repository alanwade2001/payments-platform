package com.acme.payments.reference;

import com.acme.payments.model.Iban;
import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private final CustomerId customerId;
    private final Iban iban;

    public AccountNotFoundException(CustomerId customerId, Iban iban) {
        super(String.format("Account not found: [%s] [%s] ", customerId, iban));
        this.iban = iban;
        this.customerId = customerId;
    }
}
