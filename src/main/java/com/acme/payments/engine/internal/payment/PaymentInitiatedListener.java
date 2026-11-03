package com.acme.payments.engine.internal;

import com.acme.payments.engine.CreditTransfer;
import com.acme.payments.engine.internal.payment.EnginePaymentService;
import com.acme.payments.initiation.PaymentInitiated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class PaymentInitiatedListener {

    private final EnginePaymentService paymentService;

    @EventListener
    void on(PaymentInitiated event) {

        var creditTransfer = new CreditTransfer(
                event.paymentId().reference(),
                event.debtorIban(),
                event.creditorIban(),
                event.endToEndId(),
                event.amount(),
                event.currency(),
                event.requestedExecutionDate());

        paymentService.receive(creditTransfer);
    }
}