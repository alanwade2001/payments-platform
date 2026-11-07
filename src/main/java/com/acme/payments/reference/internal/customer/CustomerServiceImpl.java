package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class CustomerServiceImpl implements Customers, CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public Optional<CustomerDetails> getCustomer(CustomerId id) {
        return repository.findById(id)
                .map(mapper::toDetails);
    }

    @Override
    public CustomerId create(String name) {
        var entity = CustomerEntity.builder()
                .name(name)
                .status(CustomerStatus.ACTIVE)
                .build();

        return repository.save(entity).id();
    }

}
