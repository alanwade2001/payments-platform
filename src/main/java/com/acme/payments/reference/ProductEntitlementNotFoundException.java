package com.acme.payments.reference;

import lombok.Getter;

@Getter
public class ProductEntitlementNotFoundException extends RuntimeException {

    private final CustomerId customerId;
    private final ProductCode productCode;

    public ProductEntitlementNotFoundException(CustomerId customerId, ProductCode productCode) {
        super(String.format("Product Entitlement not found: [%s] [%s]", customerId, productCode ));
        this.customerId = customerId;
        this.productCode = productCode;
    }


}
