package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructedTransactionRepository extends JpaRepository<InstructedTransactionEntity, Long> {
    default Optional<InstructedTransactionEntity> findById(InstructedTransactionId id) {
        return findById(Long.parseLong(id.value()));
    }
}
