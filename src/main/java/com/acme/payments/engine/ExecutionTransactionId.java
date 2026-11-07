package com.acme.payments.engine;

import com.acme.payments.engine.internal.EngineTypes;
import com.acme.payments.initiation.internal.InitiationTypes;

public record ExecutionTransactionId(String value) implements EngineId{
    @Override
    public EngineTypes type() {
        return EngineTypes.ExecutionTransaction;
    }
}
