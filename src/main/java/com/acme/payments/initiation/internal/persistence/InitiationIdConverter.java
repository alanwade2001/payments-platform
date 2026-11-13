package com.acme.payments.initiation.internal.persistence;

import com.acme.payments.initiation.InitiationId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InitiationIdConverter implements AttributeConverter<InitiationId, Long> {
    @Override
    public Long convertToDatabaseColumn(InitiationId attribute) {
        if( attribute == null) return null;
        return Long.valueOf(attribute.value());
    }

    @Override
    public InitiationId convertToEntityAttribute(Long dbData) {
        if( dbData == null) return null;
        return new InitiationId(dbData + "");
    }
}
