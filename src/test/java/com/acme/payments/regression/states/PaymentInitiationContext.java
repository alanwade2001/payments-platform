package com.acme.payments.regression.states;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.initiation.InitiationDetails;
import com.acme.payments.initiation.InitiationId;
import com.acme.payments.reference.AccountDetails;
import com.acme.payments.reference.CustomerDetails;
import com.acme.payments.regression.Pain001TestData;
import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
@Data
public class PaymentInitiationContext {

    private Pain001TestData pain001;
    private CustomerDetails customer;
    private AccountDetails account;

    private InitiationId initiationId;
    private InitiationDetails initiationDetails;
    private InstructedTransaction instructedTransaction;
    private ExecutionTransaction executionTransaction;

    private Throwable submittedException;


    public void reset() {
        this.pain001 = null;
    }




}
