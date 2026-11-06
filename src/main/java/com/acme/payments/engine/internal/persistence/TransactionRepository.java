package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;

import java.util.Optional;

public interface TransactionRepository {
    InstructedTransactionId save(InstructedTransactionEntity entity);
    Optional<InstructedTransactionEntity> findById(InstructedTransactionId id);

    ExecutionTransactionId save(ExecutionTransactionEntity entity);
    Optional<ExecutionTransactionEntity> findById(ExecutionTransactionId id);
}
