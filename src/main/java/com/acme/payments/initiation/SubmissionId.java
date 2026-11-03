package com.acme.payments.initiation;

public record SubmissionId(long value) {
    public String reference() {
        return "SUB-" + value;
    }
}
