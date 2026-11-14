package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.SchemeId;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import com.acme.payments.engine.schemes.Scheme;
import com.acme.payments.engine.schemes.SchemeRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final SchemeRegistry schemeRegistry;
    private final ExecutionTransactionRepository transactionRepository;


    public void execute(
            SchemeId schemeId,
            List<ExecutionTransaction> transactions) {

        if (transactions.isEmpty()) {
            return;
        }

        markAsExecuting(transactions);

        try {
            Scheme scheme = schemeRegistry.get(schemeId);

            scheme.execute(transactions);

            markAsExecuted(transactions);

        } catch (Exception exception) {

            markAsFailed(transactions);

            throw exception;
        }
    }

    private void markAsExecuting(List<ExecutionTransaction> transactions) {
        markAs(transactions,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);
    }

    private void markAsExecuted(List<ExecutionTransaction> transactions) {
        markAs(transactions,
                ExecutionStatus.EXECUTED,
                ExecutionStatus.EXECUTING);
    }

    private void markAsFailed(List<ExecutionTransaction> transactions) {
        markAs(transactions,
                ExecutionStatus.FAILED,
                ExecutionStatus.EXECUTING);
    }

    private void markAs(
            List<ExecutionTransaction> transactions,
            ExecutionStatus newStatus,
            ExecutionStatus oldStatus) {

        var ids = transactions.stream().map(ExecutionTransaction::id).toList();

        // update through repository
        int updated = transactionRepository.updateStatusById(ids, newStatus, oldStatus);

        if(updated != transactions.size()) {
            throw new RuntimeException("Not all transactions were updated as expected");
        }
    }
}
