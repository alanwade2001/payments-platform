package com.acme.payments.reference;

import com.acme.payments.model.Iban;

public interface Accounts {
    AccountDetails getAccount(
            CustomerId customerId,
            Iban iban);
}
