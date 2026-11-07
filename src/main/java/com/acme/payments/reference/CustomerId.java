package com.acme.payments.reference;

import com.acme.payments.reference.internal.ReferenceTypes;

public record CustomerId(String value) implements ReferenceId {

    @Override
    public ReferenceTypes type() {
        return ReferenceTypes.Customer;
    }
}
