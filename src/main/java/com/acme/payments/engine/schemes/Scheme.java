package com.acme.payments.engine.schemes;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;
import com.acme.payments.engine.SchemeId;

import java.util.List;

public interface Scheme {
    SchemeId id();
    ExecutionMode executionMode();
    ExecutionTransaction prepare(InstructedTransaction transaction, InstructedTransactionId id);
    void execute( List<ExecutionTransaction> transactions );
}
