package com.acme.payments.reference;

import com.acme.payments.reference.internal.ReferenceTypes;

public record ProductEntitlementId(String value)implements ReferenceId{

    @Override
    public ReferenceTypes type() {
        return ReferenceTypes.ProductEntitlement;
    }
}
