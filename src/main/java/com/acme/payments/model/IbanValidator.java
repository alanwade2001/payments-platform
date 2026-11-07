package com.acme.payments.model;

public interface IbanValidator {
    boolean isValid(Iban iban);
}
