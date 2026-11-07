package com.acme.payments.reference;

import com.acme.payments.reference.internal.ReferenceTypes;

public interface ReferenceId {
    String value();
    ReferenceTypes type();
    default String reference() {
        return type().reference(value());
    }
}
