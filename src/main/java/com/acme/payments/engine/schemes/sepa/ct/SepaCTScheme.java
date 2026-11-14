package com.acme.payments.engine.schemes.sepa.ct;

import com.acme.payments.engine.*;
import com.acme.payments.engine.schemes.ExecutionMode;
import com.acme.payments.engine.schemes.Scheme;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SepaCTScheme implements Scheme {

    private final SepaCTPacs008Mapper pacs008Mapper;
    private final SepaCTGateway sepaCTGateway;
    private final SepaCTMarshaller sepaCTMarshaller;


    @Override
    public SchemeId id() {
        return new  SchemeId("SCT");
    }

    @Override
    public ExecutionMode executionMode() {
        return ExecutionMode.scheduled;
    }

    @Override
    public ExecutionTransaction prepare(InstructedTransaction transaction, InstructedTransactionId instructedTransactionId) {
        return new ExecutionTransaction(null,
                instructedTransactionId,
                transaction.metadata().transactionId(),
                id(),
                transaction.requestedExecutionDate(),
                transaction.cdtTrfTxInf(),
                ExecutionStatus.READY
                );
    }

    @SneakyThrows
    @Override
    public void execute(List<ExecutionTransaction> transactions) {
        if( transactions.isEmpty() ) {
            throw new RuntimeException("There is no transactions to execute");
        }

        var document = pacs008Mapper.map(transactions);

        var xml = sepaCTMarshaller.marshal(document);

        sepaCTGateway.send(xml);

    }
}
