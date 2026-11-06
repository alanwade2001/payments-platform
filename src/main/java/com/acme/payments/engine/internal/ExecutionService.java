package com.acme.payments.engine.internal;

import com.acme.payments.engine.*;
import com.acme.payments.engine.controls.ControlResult;
import com.acme.payments.engine.internal.persistence.TransactionMapper;
import com.acme.payments.engine.internal.persistence.TransactionRepository;
import com.acme.payments.engine.schemes.Scheme;
import com.acme.payments.engine.schemes.SchemeRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExecutionService {

    private final SchemeRegistry schemeRegistry;
    private final PaymentControlService paymentControlService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void accept( InstructedTransaction transaction ) {

        var controlResult = paymentControlService.evaluate(transaction);

        InstructedTransactionId itId = persist(transaction, controlResult);
        log.info("Accepted instructed transaction {}", itId);

        if(controlResult.passed()) {
            ExecutionTransactionId exId = prepare( transaction, itId );
            log.info("Accepted execution transaction {}", exId);
        }
    }

    public InstructedTransactionId persist(InstructedTransaction transaction, ControlResult result ){

        var entity = transactionMapper.toEntity(transaction);

        var status = result.passed() ? InstructedTransactionStatus.ACCEPTED : InstructedTransactionStatus.REJECTED;
        entity.setStatus(status);

        return transactionRepository.save(entity);
    }


    public ExecutionTransactionId prepare(InstructedTransaction transaction,  InstructedTransactionId id ) {
        Scheme scheme = schemeRegistry.get(transaction.metadata().schemeId());
        var executionTransaction = scheme.prepare(transaction, id);
        var entity = transactionMapper.toEntity(executionTransaction);
        return transactionRepository.save(entity);
    }
}
