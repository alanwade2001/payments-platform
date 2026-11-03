package com.acme.payments.reference;

import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {

    private final CustomerId customerId;

    public CustomerNotFoundException(CustomerId customerId) {
        super("Customer not found: " + customerId);
        this.customerId = customerId;
    }

}
