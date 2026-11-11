package com.acme.payments.reference.internal.product;

import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.ProductCode;
import com.acme.payments.reference.ProductEntitlement;
import com.acme.payments.reference.ProductEntitlementId;

import java.util.List;

public interface ProductEntitlementService {
    ProductEntitlement getProductEntitlement(
            CustomerId customerId,
            ProductCode productCode);

    ProductEntitlementId addProductEntitlement(
            CustomerId customerId,
            ProductCode productCode);

    List<ProductEntitlement> getProductEntitlements(CustomerId customerId);
}
