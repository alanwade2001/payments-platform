package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByIdAndCustomerId(AccountId id, CustomerId customerId);

    Optional<AccountEntity> findByIbanAndCustomerId(Iban iban, CustomerId customerId);

    default Optional<AccountEntity> findById(AccountId id){
        try{
            return findById(Long.parseLong(id.value()));
        } catch(NumberFormatException e){
            return Optional.empty();
        }
    }


    Optional<AccountEntity> findByIban(Iban iban);
}
