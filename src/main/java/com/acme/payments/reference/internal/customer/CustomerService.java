package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;

import java.util.Optional;

public interface CustomerService {
    Optional<CustomerDetails> getCustomer(CustomerId id);

    CustomerId create(String name);
}
