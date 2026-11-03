package com.acme.payments.engine;

public record EnginePaymentId(long value) {

    public String reference() {
        return "EPAY-" + value;
    }

    @Override
    public String toString() {
        return reference();
    }
}
