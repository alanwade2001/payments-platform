package com.acme.payments.engine;

public record SchemeId(
        String value
) {
    public String reference() {
        return "SCH-" + value;
    }
}
