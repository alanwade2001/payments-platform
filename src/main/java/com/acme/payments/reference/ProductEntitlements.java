package com.acme.payments.reference;

public interface ProductEntitlements {
    ProductEntitlement getProductEntitlement(
            CustomerId customerId,
            ProductCode productCode);
}
