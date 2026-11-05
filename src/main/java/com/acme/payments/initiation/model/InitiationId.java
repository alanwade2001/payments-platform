package com.acme.payments.initiation.model;

public record InitiationId(String value) {
    public String reference() {
        return "INIT-" + value;
    }
}
