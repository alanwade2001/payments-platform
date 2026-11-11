package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    default Optional<CustomerEntity> findById(CustomerId id) {
        // make sure it is a long
        try {
            Long lid = Long.parseLong(id.value());
            return findById(lid);
        }catch(NumberFormatException e){
            return Optional.empty();
        }
    }

    Optional<CustomerEntity> findByName(String name);
}
