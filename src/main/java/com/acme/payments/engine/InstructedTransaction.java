package com.acme.payments.engine;

import com.acme.payments.model.Iban;
import iso.std.iso._20022.tech.xsd.pacs_008_001.CreditTransferTransaction73;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstructedTransaction(
        InstructionMetadata metadata,
        CreditTransferTransaction73 cdtTrfTxInf
) {
    public Iban creditorIban() {
        String iban = cdtTrfTxInf.getCdtrAcct().getId().getIBAN();
        return new Iban(iban);
    }

    public Iban debtorIban() {
        String iban = cdtTrfTxInf.getDbtrAcct().getId().getIBAN();
        return new Iban(iban);
    }

    public BigDecimal amount() {
        return cdtTrfTxInf.getIntrBkSttlmAmt().getValue();
    }

    public LocalDate requestedExecutionDate() {
        return cdtTrfTxInf.getIntrBkSttlmDt();
    }
}
