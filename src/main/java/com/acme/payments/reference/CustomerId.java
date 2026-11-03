package com.acme.payments.reference;

public record CustomerId(long value) {
    public String reference() {
        return "CUST-" + value;
    }

    @Override
    public String toString() {
        return reference();
    }
}
