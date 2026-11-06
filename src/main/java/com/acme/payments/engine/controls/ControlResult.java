package com.acme.payments.engine.controls;

public record ControlResult(
        ControlDecision decision,
        String reason
) {
    public static ControlResult pass() {
        return new ControlResult(ControlDecision.Pass, "");
    }

    public static ControlResult reject(String reason) {
        return new ControlResult(ControlDecision.Reject, reason);
    }

    public boolean rejected(){
        return decision.equals(ControlDecision.Reject);
    }

    public boolean passed() {
        return decision.equals(ControlDecision.Pass);

    }
}
