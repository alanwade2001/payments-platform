package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import com.acme.payments.engine.internal.persistence.InstructedTransactionRepository;
import com.acme.payments.engine.internal.persistence.TransactionMapper;
import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.reference.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/engine")
public class EngineAdminController {

    private final TransactionMapper transactionMapper;
    private final InstructedTransactionRepository instructedTransactionRepository;
    private final ExecutionTransactionRepository  executionTransactionRepository;

    @GetMapping("/instructed-transactions")
    public List<InstructedTransaction> getInstructedTransactions() {
        return instructedTransactionRepository.findAll().stream().map(transactionMapper::fromEntity).toList();
    }

    @GetMapping("/instructed-transactions/{id}")
    public Optional<InstructedTransaction> getInstructedTransaction(@PathVariable InstructedTransactionId id) {
        return instructedTransactionRepository.findById(id).map(transactionMapper::fromEntity);
    }

    @GetMapping("/execution-transactions")
    public List<ExecutionTransaction> getExecutionTransactions() {
        return executionTransactionRepository.findAll().stream().map(transactionMapper::fromEntity).toList();
    }

    @GetMapping("/execution-transactions/{id}")
    public Optional<ExecutionTransaction> getExecutionTransaction(@PathVariable ExecutionTransactionId id) {
        return executionTransactionRepository.findById(id).map(transactionMapper::fromEntity);
    }
}
