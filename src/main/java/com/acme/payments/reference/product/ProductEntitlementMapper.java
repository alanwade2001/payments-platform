package com.acme.payments.reference.product;

import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.ProductEntitlement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntitlementMapper {

    ProductEntitlement toEntitlement(
            ProductEntitlementEntity entity);

    default CustomerId toCustomerId(Long id) {
        return id == null ? null : new CustomerId(id);
    }
}
