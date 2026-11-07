package com.acme.payments.engine.internal;

public enum EngineTypes {
    ExecutionTransaction("execution_transaction"),
    InstructedTransaction("instructed_transaction"),
    Scheme("scheme"),
    Transaction("transaction");

    private final String prefix;
    EngineTypes(String prefix) {
        this.prefix = prefix;
    }

    public String reference(Object value) {
        return prefix + "-" + value;
    }
}
