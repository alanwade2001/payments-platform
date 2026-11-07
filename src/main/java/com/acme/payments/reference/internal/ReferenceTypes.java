package com.acme.payments.reference.internal;

public enum ReferenceTypes {
    Customer("customer"),
    Account("account"),
    ProductEntitlement("product_entitlement"),;

    private final String prefix;
    ReferenceTypes(String prefix) {
        this.prefix = prefix;
    }

    public String reference(Object value) {
        return prefix + "-" + value;
    }
}
