package com.acme.payments.engine.internal.controllers;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;
import com.acme.payments.engine.internal.ExportInstruction;
import com.acme.payments.engine.internal.ExportService;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import com.acme.payments.engine.internal.persistence.InstructedTransactionRepository;
import com.acme.payments.engine.internal.persistence.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/engine")
public class EngineAdminController {

    private final TransactionMapper transactionMapper;
    private final InstructedTransactionRepository instructedTransactionRepository;
    private final ExecutionTransactionRepository  executionTransactionRepository;
    private final ExportService exportService;

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

    @PostMapping("/export")
    void export(@RequestBody ExportInstruction instruction) {
        exportService.export(instruction);
    }
}
