package com.acme.payments.engine.controls.mule;

import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.controls.ControlResult;
import com.acme.payments.engine.controls.PaymentControl;
import com.acme.payments.model.Iban;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MuleAccountControl implements PaymentControl {
    private final MuleAccountService muleAccountService;

    @Override
    public ControlResult evaluate(InstructedTransaction transaction) {
        Iban debtorAcct = transaction.debtorIban();
        Iban creditorAcct = transaction.creditorIban();

        var result = checkIban(debtorAcct);

        if(result.passed()) {
            result = checkIban(creditorAcct);
        }

        return result;
    }

    private ControlResult checkIban(Iban iban) {
        if( muleAccountService.isMuleAccount( iban ) ) {
            return ControlResult.reject(String.format("Mule account [%s]", iban.toString()));
        }

        return ControlResult.pass();
    }
}
