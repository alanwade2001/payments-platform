package com.acme.payments.reference.internal.product;

import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ProductEntitlementServiceImpl
        implements ProductEntitlements, ProductEntitlementService {

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

    @Override
    public ProductEntitlementId addProductEntitlement(CustomerId customerId, ProductCode productCode) {
        var entity = ProductEntitlementEntity.builder()
                .customerId(customerId)
                .productCode(productCode)
                .status(EntitlementStatus.ACTIVE)
                .build();

        return repository.save(entity).id();
    }
}
