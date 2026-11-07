package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    default Optional<CustomerEntity> findById(CustomerId id) {
        return findById(Long.parseLong(id.value()));
    }
}
