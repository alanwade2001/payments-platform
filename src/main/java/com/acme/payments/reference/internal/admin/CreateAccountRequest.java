package com.acme.payments.reference.internal.admin;

public record CreateAccountRequest(
        String iban,
        String currency
) {
}
