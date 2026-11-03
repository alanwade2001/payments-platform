package com.acme.payments.reference;

public interface ProductEntitlements {
    ProductEntitlement getEntitlement(
            CustomerId customerId,
            ProductCode productCode);
}
