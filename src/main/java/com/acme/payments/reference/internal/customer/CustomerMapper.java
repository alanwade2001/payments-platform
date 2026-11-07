package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "customerId", source = "id")
    CustomerDetails toDetails(CustomerEntity entity);

    default CustomerId toCustomerId(String id) {
        return id == null ? null : new CustomerId(id);
    }
}
