package com.acme.payments.reference;

public interface Accounts {
    AccountDetails getAccount(
            CustomerId customerId,
            Iban iban);
}
