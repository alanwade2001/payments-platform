package com.acme.payments.reference;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AccountId(String value) {

    public String reference() {
        return "ACCT-" + value;
    }

    @Override
    public String toString() {
        return reference();
    }
}
