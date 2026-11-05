package com.acme.payments.reference.product;

import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ProductEntitlementService
        implements ProductEntitlements {

    private final ProductEntitlementRepository repository;
    private final ProductEntitlementMapper mapper;

    @Override
    public ProductEntitlement getProductEntitlement(
            CustomerId customerId,
            ProductCode productCode) {

        return repository
                .findByCustomerIdAndProductCode(
                        Long.parseLong(customerId.value()),
                        productCode)
                .map(mapper::toEntitlement)
                .orElseThrow(() ->
                        new ProductEntitlementNotFoundException(
                                customerId,
                                productCode));
    }
}
