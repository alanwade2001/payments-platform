package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AccountServiceImpl implements Accounts, AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public AccountDetails getAccount(
            CustomerId customerId,
            Iban iban) {

        return repository
                .findByIbanAndCustomerId(
                        iban,
                        customerId)
                .map(mapper::toDetails)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                customerId,
                                iban));
    }

    @Override
    public Optional<AccountDetails> getAccount(AccountId accountId) {
        return repository.findById(accountId).map(mapper::toDetails);
    }

    @Override
    public AccountId addAccount(CustomerId customerId, Iban iban, String currency) {
        var entity = AccountEntity.builder()
                .currency(currency)
                .iban(iban)
                .customerId(customerId)
                .status(AccountStatus.ACTIVE)
                .build();

        return repository.save(entity).id();
    }
}
