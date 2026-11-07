package com.acme.payments.engine;

import com.acme.payments.engine.internal.EngineTypes;

public record InstructedTransactionId(String value) implements EngineId{
    @Override
    public EngineTypes type() {
        return EngineTypes.InstructedTransaction;
    }
}
