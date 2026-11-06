package com.acme.payments.engine.schemes.sepa.ct;

import com.acme.payments.engine.*;
import com.acme.payments.engine.schemes.ExecutionMode;
import com.acme.payments.engine.schemes.Scheme;
import org.springframework.stereotype.Component;

@Component
public class SepaCTScheme implements Scheme {
    @Override
    public SchemeId id() {
        return new  SchemeId("SCT");
    }

    @Override
    public ExecutionMode executionMode() {
        return ExecutionMode.scheduled;
    }

    @Override
    public ExecutionTransaction prepare(InstructedTransaction transaction, InstructedTransactionId instructedTransactionId) {
        return new ExecutionTransaction(null,
                instructedTransactionId,
                transaction.metadata().transactionId(),
                id(),
                transaction.requestedExecutionDate(),
                transaction.cdtTrfTxInf(),
                ExecutionStatus.READY
                );
    }

    @Override
    public void execute(ExecutionTransaction transaction) {

    }
}
