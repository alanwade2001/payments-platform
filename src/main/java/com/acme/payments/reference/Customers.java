package com.acme.payments.reference;

import java.util.Optional;

public interface Customers {
    Optional<CustomerDetails> getCustomer(CustomerId customerId);

}
