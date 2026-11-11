package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.AccountDetails;
import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.IdOrName;

import java.util.Optional;

public interface AccountService {
    AccountDetails getAccount(
            CustomerId customerId,
            Iban iban);

    Optional<AccountDetails> getAccount(
            AccountId accountId);

    Optional<AccountDetails> getAccount(
            IdOrName idOrName);

    AccountId addAccount(CustomerId customerId, Iban iban, String currency);
}
