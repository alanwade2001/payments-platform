package com.acme.payments.engine.internal;

import com.acme.payments.engine.*;
import com.acme.payments.engine.controls.ControlResult;
import com.acme.payments.engine.internal.persistence.*;
import com.acme.payments.engine.schemes.ExecutionMode;
import com.acme.payments.engine.schemes.Scheme;
import com.acme.payments.engine.schemes.SchemeRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentAcceptanceService {

    private final SchemeRegistry schemeRegistry;
    private final PaymentControlService paymentControlService;
    private final InstructedTransactionRepository transactionRepository;
    private final ExecutionTransactionRepository executionTransactionRepository;
    private final TransactionMapper transactionMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void accept( InstructedTransaction transaction ) {

        var controlResult = paymentControlService.evaluate(transaction);

        InstructedTransactionId itId = persist(transaction, controlResult);
        log.info("Accepted instructed transaction {}", itId);

        if(controlResult.passed()) {
            ExecutionTransactionId exId = prepare( transaction, itId );
            log.info("Accepted execution transaction {}", exId);

            Scheme scheme = getScheme(transaction);
            if( scheme.executionMode().equals(ExecutionMode.instant)) {
                applicationEventPublisher.publishEvent(new ImmediateExecutionRequested(exId));
            }
        }
    }

    public InstructedTransactionId persist(InstructedTransaction transaction, ControlResult result ){

        InstructedTransactionEntity entity = transactionMapper.toEntity(transaction);

        var status = result.passed() ? InstructedTransactionStatus.ACCEPTED : InstructedTransactionStatus.REJECTED;
        entity.setStatus(status);

        var saved = transactionRepository.save(entity);

        return saved.id();
    }


    public ExecutionTransactionId prepare(InstructedTransaction transaction,  InstructedTransactionId id ) {
        Scheme scheme = getScheme(transaction);
        var executionTransaction = scheme.prepare(transaction, id);
        ExecutionTransactionEntity entity = transactionMapper.toEntity(executionTransaction);
        var saved = executionTransactionRepository.save(entity);
        return saved.id();
    }

    private Scheme getScheme( InstructedTransaction transaction ) {
        return schemeRegistry.get(transaction.metadata().schemeId());
    }
}
