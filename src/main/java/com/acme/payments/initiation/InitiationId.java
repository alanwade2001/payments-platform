package com.acme.payments.initiation;

public record InitiationId(String value) {
    public String reference() {
        return "INIT-" + value;
    }
}
