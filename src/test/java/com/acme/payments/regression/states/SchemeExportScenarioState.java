package com.acme.payments.regression.states;

import com.acme.payments.engine.ExecutionTransactionId;
import io.cucumber.spring.ScenarioScope;
import iso.std.iso._20022.tech.xsd.pacs_008_001.Document;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@ScenarioScope
@Data
public class SchemeExportScenarioState {

    private final List<ExecutionTransactionId> executionTransactionIds =
            new ArrayList<>();

    private LocalDate executionDate;
    private Document pacs008;

    public List<ExecutionTransactionId> executionTransactionIds() {
        return List.copyOf(executionTransactionIds);
    }

    public void executionTransactionIds(
            List<ExecutionTransactionId> transactionIds) {

        executionTransactionIds.clear();
        executionTransactionIds.addAll(transactionIds);
    }

    public void reset() {
        executionTransactionIds.clear();
        executionDate = null;
        pacs008 = null;
    }
}
