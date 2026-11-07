package com.acme.payments.reference;

import com.acme.payments.reference.internal.ReferenceTypes;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AccountId(String value) implements ReferenceId {

    @Override
    public ReferenceTypes type() {
        return ReferenceTypes.Account;
    }
}
