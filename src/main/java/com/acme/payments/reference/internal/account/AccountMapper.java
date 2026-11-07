package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.AccountDetails;
import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.CustomerId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountId", source = "id")
    AccountDetails toDetails(AccountEntity entity);

    default AccountId toAccountId(String id) {
        return id == null ? null : new AccountId(id);
    }

    default CustomerId toCustomerId(String id) {
        return id == null ? null : new CustomerId(id);
    }

    default Iban toIban(String iban) {return iban == null ? null : new Iban(iban);}
}
