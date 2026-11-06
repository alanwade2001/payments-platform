package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionTransactionRepository extends JpaRepository<ExecutionTransactionEntity, Long> {
}
