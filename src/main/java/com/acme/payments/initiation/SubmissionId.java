package com.acme.payments.initiation;

public record SubmissionId(String value) {
    public String reference() {
        return "SUB-" + value;
    }
}
