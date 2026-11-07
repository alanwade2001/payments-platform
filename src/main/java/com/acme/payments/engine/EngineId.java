package com.acme.payments.engine;

import com.acme.payments.engine.internal.EngineTypes;


public interface EngineId {
    String value();
    EngineTypes type();
    default String reference() {
        return type().reference(value());
    }
}
