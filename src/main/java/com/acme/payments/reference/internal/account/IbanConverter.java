package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IbanConverter implements AttributeConverter<Iban, String> {
    @Override
    public String convertToDatabaseColumn(Iban attribute) {
        if( attribute == null) {return null;}
        return attribute.value();
    }

    @Override
    public Iban convertToEntityAttribute(String dbData) {
        if(dbData == null) {return null;}
        return new Iban(dbData);
    }
}
