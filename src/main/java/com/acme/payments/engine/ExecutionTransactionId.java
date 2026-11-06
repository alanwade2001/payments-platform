package com.acme.payments.engine;

public record ExecutionTransactionId(String value) {
    public String reference() {
        return "EXTX-" + value;
    }
}
