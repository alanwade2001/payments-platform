package com.acme.payments.model;

public record Iban(String value) {
    public static Iban of(String value) {
        return new Iban(value);
    }
}
