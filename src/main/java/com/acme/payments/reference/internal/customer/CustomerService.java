package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.IdOrName;

import java.util.Optional;

public interface CustomerService {
    Optional<CustomerDetails> getCustomer(CustomerId id);
    Optional<CustomerDetails> getCustomer(IdOrName idOrName);

    CustomerId create(String name);
}
