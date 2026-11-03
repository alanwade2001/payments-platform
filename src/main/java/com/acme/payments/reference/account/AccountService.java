package com.acme.payments.reference.account;

import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccountService implements Accounts {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public AccountDetails getAccount(
            CustomerId customerId,
            Iban iban) {

        return repository
                .findByIbanAndCustomerId(
                        iban.value(),
                        customerId.value())
                .map(mapper::toDetails)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                customerId,
                                iban));
    }
}
