package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.SchemeId;
import com.acme.payments.reference.CustomerId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SchemeIdConverter implements AttributeConverter<SchemeId, String> {
    @Override
    public String convertToDatabaseColumn(SchemeId attribute) {
        if( attribute == null) return null;
        return attribute.value();
    }

    @Override
    public SchemeId convertToEntityAttribute(String dbData) {
        if( dbData == null) return null;
        return new SchemeId(dbData);
    }
}
