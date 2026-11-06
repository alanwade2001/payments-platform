package com.acme.payments.engine.internal;

import com.acme.payments.engine.InstructedTransaction;
import com.acme.payments.engine.controls.ControlResult;
import com.acme.payments.engine.controls.PaymentControl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentControlService {

    private final List<PaymentControl> controls;

    public PaymentControlService(List<PaymentControl> controls) {
        this.controls = controls;
    }

    public ControlResult evaluate(InstructedTransaction transaction) {
        for (PaymentControl control : controls) {
            ControlResult result = control.evaluate(transaction);

            if (result.rejected()) {
                return result;
            }
        }

        return ControlResult.pass();
    }
}
