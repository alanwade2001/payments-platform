package com.acme.payments.reference;

public record CustomerDetails(
        CustomerId customerId,
        String name,
        CustomerStatus status
) {
}
