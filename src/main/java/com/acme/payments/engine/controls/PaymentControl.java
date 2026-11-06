package com.acme.payments.engine.controls;


import com.acme.payments.engine.InstructedTransaction;

public interface PaymentControl {
    ControlResult evaluate(InstructedTransaction transaction);
}
