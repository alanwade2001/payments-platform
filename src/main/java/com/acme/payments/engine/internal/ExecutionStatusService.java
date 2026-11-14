package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutionStatusService {

    private final ExecutionTransactionRepository transactionRepository;

    @Transactional
    public void markAs(
            List<ExecutionTransaction> transactions,
            ExecutionStatus newStatus,
            ExecutionStatus oldStatus) {

        var ids = transactions.stream()
                .map(ExecutionTransaction::id)
                .toList();

        int updated = transactionRepository.updateStatusById(
                ids,
                newStatus,
                oldStatus);

        if (updated != transactions.size()) {
            throw new IllegalStateException(
                    "Expected %d transactions to move from %s to %s, but %d were updated"
                            .formatted(
                                    transactions.size(),
                                    oldStatus,
                                    newStatus,
                                    updated));
        }
    }
}
