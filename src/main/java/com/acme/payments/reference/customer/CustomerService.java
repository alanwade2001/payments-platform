package com.acme.payments.reference.customer;

import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.CustomerNotFoundException;
import com.acme.payments.reference.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CustomerService implements Customers {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerDetails getCustomer(CustomerId id) {
        return repository.findById(id.value())
                .map(mapper::toDetails)
                .orElseThrow(() ->
                        new CustomerNotFoundException(id));
    }
}
