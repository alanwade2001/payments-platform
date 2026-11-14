package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import com.acme.payments.engine.internal.persistence.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExportService {
    private final ExecutionTransactionRepository transactionRepository;
    private final ExecutionService executionService;
    private final TransactionMapper transactionMapper;

    public void export(ExportInstruction instruction) {

        var transactions =
                transactionRepository.findExecutionForStatus(
                        instruction.schemeId(),
                        ExecutionStatus.READY,
                        instruction.executionDate())
                        .stream()
                        .map(transactionMapper::fromEntity)
                        .toList();

        executionService.execute(
                instruction.schemeId(),
                transactions);
    }
}
