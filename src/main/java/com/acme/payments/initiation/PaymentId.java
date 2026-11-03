package com.acme.payments.initiation;

public record PaymentId(long value) {
    public String reference() {
        return "PMT-" + value;
    }
}
