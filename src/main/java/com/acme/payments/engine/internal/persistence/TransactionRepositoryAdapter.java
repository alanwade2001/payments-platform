package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final ExecutionTransactionRepository executionTransactionRepository;
    private final InstructedTransactionRepository instructedTransactionRepository;

    @Override
    public InstructedTransactionId save(InstructedTransactionEntity entity) {
        var saved = instructedTransactionRepository.save(entity);
        return saved.id();
    }

    @Override
    public Optional<InstructedTransactionEntity> findById(InstructedTransactionId id) {
        long lid = Long.parseLong(id.value());
        return instructedTransactionRepository.findById(lid);
    }

    @Override
    public ExecutionTransactionId save(ExecutionTransactionEntity entity) {
        var saved = executionTransactionRepository.save(entity);
        return saved.id();
    }

    @Override
    public Optional<ExecutionTransactionEntity> findById(ExecutionTransactionId id) {
        long lid = Long.parseLong(id.value());
        return executionTransactionRepository.findById(lid);
    }
}
