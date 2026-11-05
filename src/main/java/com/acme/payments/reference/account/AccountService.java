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

        long lid = Long.parseLong(customerId.value());

        return repository
                .findByIbanAndCustomerId(
                        iban.value(),
                        lid)
                .map(mapper::toDetails)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                customerId,
                                iban));
    }
}
