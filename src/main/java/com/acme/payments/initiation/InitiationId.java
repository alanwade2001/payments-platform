package com.acme.payments.initiation;

import com.acme.payments.initiation.internal.InitiationTypes;

public record InitiationId(String value) implements InitiationBaseId {
    @Override
    public InitiationTypes type() {
        return InitiationTypes.Initiation;
    }
}
