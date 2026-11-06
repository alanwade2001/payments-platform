package com.acme.payments.engine;

public record InstructedTransactionId(String value) {
    public String reference() {
        return "INTX-" + value;
    }
}
