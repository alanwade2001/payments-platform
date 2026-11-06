package com.acme.payments.engine;

public record TransactionId(String value) {
    public String reference() {
        return "TX-" + value;
    }
}
