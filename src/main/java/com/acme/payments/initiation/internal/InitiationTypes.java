package com.acme.payments.initiation.internal;

public enum InitiationTypes {
    Initiation("initiation"),
    Payment("payment"),
    Transaction("transaction");

    private final String prefix;
    InitiationTypes(String prefix) {
        this.prefix = prefix;
    }

    public String reference(Object value) {
        return prefix + "-" + value;
    }
}
