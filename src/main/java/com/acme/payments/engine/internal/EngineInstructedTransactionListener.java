package com.acme.payments.engine.internal;

import com.acme.payments.engine.InstructedTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class EngineInstructedTransactionListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutionService executionService;

    @EventListener(InstructedTransaction.class)
    public void handleInstructedTransactionEvent(InstructedTransaction instructedTransaction) {
        log.info("Handling InstructedTransaction: {}", objectMapper.writeValueAsString(instructedTransaction));

        executionService.accept(instructedTransaction);
    }
}
