package com.acme.payments.reference.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByIdAndCustomerId(long id, long customerId);

    Optional<AccountEntity> findByIbanAndCustomerId(String iban, long customerId);
}
