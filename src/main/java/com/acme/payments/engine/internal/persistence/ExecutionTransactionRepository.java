package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExecutionTransactionRepository extends JpaRepository<ExecutionTransactionEntity, Long> {
    default Optional<ExecutionTransactionEntity> findById(ExecutionTransactionId id) {
        return findById(Long.parseLong(id.value()));
    }
}
