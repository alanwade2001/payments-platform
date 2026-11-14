package com.acme.payments.engine.internal;

import com.acme.payments.engine.InstructedTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class EngineInstructedTransactionListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentAcceptanceService paymentAcceptanceService;

    @EventListener(InstructedTransaction.class)
    public void handleInstructedTransactionEvent(InstructedTransaction instructedTransaction) {
        log.info("Handling InstructedTransaction: {}", objectMapper.writeValueAsString(instructedTransaction));

        paymentAcceptanceService.accept(instructedTransaction);
    }
}
