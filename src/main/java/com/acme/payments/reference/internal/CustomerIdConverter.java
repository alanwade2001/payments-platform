package com.acme.payments.reference.internal;

import com.acme.payments.reference.CustomerId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CustomerIdConverter implements AttributeConverter<CustomerId, Long> {
    @Override
    public Long convertToDatabaseColumn(CustomerId attribute) {
        if( attribute == null) return null;
        return Long.valueOf(attribute.value());
    }

    @Override
    public CustomerId convertToEntityAttribute(Long dbData) {
        if( dbData == null) return null;
        return new CustomerId(dbData + "");
    }
}
