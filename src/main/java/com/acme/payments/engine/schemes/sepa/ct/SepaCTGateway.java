package com.acme.payments.engine.schemes.sepa.ct;

public interface SepaCTGateway {
    void send(byte[] pacs008);
}
