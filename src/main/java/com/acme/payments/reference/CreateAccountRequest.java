package com.acme.payments.reference;

public record CreateAccountRequest(
        String iban,
        String currency
) {
}
