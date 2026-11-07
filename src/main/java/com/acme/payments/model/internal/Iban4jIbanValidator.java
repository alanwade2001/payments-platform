package com.acme.payments.model.internal;

import com.acme.payments.model.Iban;
import com.acme.payments.model.IbanValidator;
import org.iban4j.IbanUtil;

public class Iban4jIbanValidator implements IbanValidator{
    @Override
    public boolean isValid(Iban iban) {
        return IbanUtil.isValid(iban.value());
    }
}
