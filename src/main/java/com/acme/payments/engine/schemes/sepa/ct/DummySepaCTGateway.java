package com.acme.payments.engine.schemes.sepa.ct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DummySepaCTGateway implements SepaCTGateway {
    @Override
    public void send(byte[] pacs008) {
        log.debug("Sending PACS008");
        log.info(new String(pacs008));
    }
}
