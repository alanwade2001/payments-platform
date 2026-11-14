package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionTransactionId;

public record ImmediateExecutionRequested(
        ExecutionTransactionId id
) {}
