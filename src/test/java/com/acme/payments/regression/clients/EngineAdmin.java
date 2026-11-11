package com.acme.payments.regression.clients;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.InstructedTransactionId;
import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class EngineAdmin {

    private final TestApiClient testApiClient;
    private final String base = "/api/admin/engine";


    public List<InstructedTransaction> getInstructedTransactions() {
        String local = "/instructed-transactions";
        String uri = base + local;

        var typeRef = new ParameterizedTypeReference<List<InstructedTransaction>>() {};

        return testApiClient.get(uri,typeRef);
    }

    public Optional<InstructedTransaction> getInstructedTransaction(String endToEndId) {
        var transactions = getInstructedTransactions();;
        return transactions
                .stream()
                .filter(x->x.cdtTrfTxInf().getPmtId().getEndToEndId()
                .equals(endToEndId))
                .findFirst();
    }

    public List<ExecutionTransaction> getExecutionTransactions() {
        String local = "/execution-transactions";
        String uri = base + local;

        var typeRef = new ParameterizedTypeReference<List<ExecutionTransaction>>() {};

        return testApiClient.get(uri,typeRef);
    }

    public Optional<ExecutionTransaction> getExecutionTransaction(InstructedTransactionId id) {
        var transactions = getExecutionTransactions();
        return transactions
                .stream()
                .filter(x->x.instructedTransactionId().equals(id))
                .findFirst();
    }
}
