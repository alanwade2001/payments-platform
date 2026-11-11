package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.*;
import com.acme.payments.reference.IdOrName;
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
    public Optional<CustomerDetails> getCustomer(IdOrName idOrName) {
        String id = idOrName.idOrName();;
        var result = getCustomer(new CustomerId(id));

        if(result.isEmpty()) {
            String name = idOrName.idOrName();
            return getCustomerByName(name);
        } else {
            return result;
        }
    }

    public Optional<CustomerDetails> getCustomerByName(String name) {
        return repository.findByName(name).map(mapper::toDetails);
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
