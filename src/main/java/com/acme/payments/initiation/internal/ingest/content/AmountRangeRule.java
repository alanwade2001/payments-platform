package com.acme.payments.initiation.internal.ingest.content;

import com.acme.payments.initiation.internal.ingest.Pain001Calculations;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmountRangeRule implements Pain001ContentRule {
    private static final BigDecimal MIN_AMOUNT =
            new BigDecimal("0.01");

    private static final BigDecimal MAX_AMOUNT =
            new BigDecimal("1000000000");

    @Override
    public List<String> validate(Document document) {

        var errors = new ArrayList<String>();
        var cstmrCdtTrfInitn = document.getCstmrCdtTrfInitn();

        for (var pmtInf : cstmrCdtTrfInitn.getPmtInves()) {

            for (var transaction : pmtInf.getCdtTrfTxInves()) {

                var amount = transaction
                        .getAmt()
                        .getInstdAmt()
                        .getValue();

                validate(
                        "tx " + transaction.getPmtId().getEndToEndId(),
                        amount,
                        errors);
            }

            var calculated =
                    Pain001Calculations.controlSum(pmtInf);

            validate(
                    "pmt inf " + pmtInf.getPmtInfId(),
                    calculated,
                    errors);
        }

        var calculated =
                Pain001Calculations.controlSum(document);

        validate("grp hdr", calculated, errors);

        return errors;
    }

    private void validate(
            String level,
            BigDecimal amount,
            List<String> errors) {

        if (amount.compareTo(MIN_AMOUNT) < 0
                || amount.compareTo(MAX_AMOUNT) > 0) {

            errors.add(
                    "%s amount: %s outside range: %s - %s"
                            .formatted(
                                    level,
                                    amount,
                                    MIN_AMOUNT,
                                    MAX_AMOUNT));
        }
    }
}
