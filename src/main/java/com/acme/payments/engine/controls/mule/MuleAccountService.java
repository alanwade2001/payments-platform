package com.acme.payments.engine.controls.mule;

import com.acme.payments.engine.Iban;
import org.springframework.stereotype.Service;

@Service
public class MuleAccountService {
    public boolean isMuleAccount( Iban iban ) {
        return false;
    }
}
