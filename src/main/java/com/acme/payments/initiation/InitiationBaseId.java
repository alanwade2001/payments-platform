package com.acme.payments.initiation;

import com.acme.payments.initiation.internal.InitiationTypes;

public interface InitiationBaseId {
    String value();
    InitiationTypes type();
    default String reference() {
        return type().reference(value());
    }
}
