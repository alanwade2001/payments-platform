package com.acme.payments.reference;

public record ProductEntitlement(
        CustomerId customerId,
        ProductCode productCode,
        EntitlementStatus status
) {
}
