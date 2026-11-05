package com.acme.payments.initiation.internal.ingest;

import iso.std.iso._20022.tech.xsd.pain_001_001.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pain001Calculations {

    public static BigDecimal controlSum(PaymentInstruction44 pmtInf) {
        return pmtInf.getCdtTrfTxInves().stream()
                .map(CreditTransferTransaction61::getAmt)
                .map(AmountType4Choice::getInstdAmt)
                .map(ActiveOrHistoricCurrencyAndAmount::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal controlSum(Document document) {
        return document.getCstmrCdtTrfInitn()
                .getPmtInves().stream()
                .map(Pain001Calculations::controlSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static long numberOfTransactions(PaymentInstruction44 pmtInf) {
        return pmtInf.getCdtTrfTxInves().size();
    }

    public static long numberOfTransactions(Document document) {
        return document.getCstmrCdtTrfInitn()
                .getPmtInves().stream()
                .mapToLong(Pain001Calculations::numberOfTransactions)
                .sum();
    }
}
