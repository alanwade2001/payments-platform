package com.acme.payments.reference.internal.account;

import com.acme.payments.reference.AccountId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountConverter implements AttributeConverter<AccountId, Long> {
    @Override
    public Long convertToDatabaseColumn(AccountId attribute) {
        if( attribute == null) {return null;}
        return Long.parseLong(attribute.value());
    }

    @Override
    public AccountId convertToEntityAttribute(Long dbData) {
        if(dbData == null) {return null;}
        return new AccountId(dbData + "");
    }
}
