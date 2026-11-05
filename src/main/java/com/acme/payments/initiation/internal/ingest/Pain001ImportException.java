package com.acme.payments.initiation.internal.ingest;

public class Pain001ImportException extends RuntimeException {
    public Pain001ImportException(String message) {
        super(message);
    }

    public Pain001ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
