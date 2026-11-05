package com.acme.payments.initiation.internal.ingest.content;

import com.acme.payments.initiation.internal.ingest.Pain001Calculations;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NumberOfTransactionsRule implements Pain001ContentRule {


        @Override
        public List<String> validate(Document document) {
            List<String> errors = new ArrayList<>();
            var initiation = document.getCstmrCdtTrfInitn();
            var paymentInstructions = initiation.getPmtInves();

            for (var pmtInf : paymentInstructions) {

                var calculatedPmtInfNbOfTxs = Pain001Calculations.numberOfTransactions(pmtInf);

                var nbOfTxs = pmtInf.getNbOfTxs();

                if (nbOfTxs != null
                        && nbOfTxs != calculatedPmtInfNbOfTxs) {

                    errors.add(
                            "pmt inf nbOfTxs: %s != calculated: %s"
                                    .formatted(nbOfTxs, calculatedPmtInfNbOfTxs));
                }
            }

            var grpHdrNbOfTxs =
                    initiation.getGrpHdr().getNbOfTxs();

            var calculatedGrpHdrNbOfTxs =
                    Pain001Calculations.numberOfTransactions(document);

            if ( grpHdrNbOfTxs != calculatedGrpHdrNbOfTxs) {

                errors.add(
                        "grp hdr nbOfTxs: %s != calculated: %s"
                                .formatted(
                                        grpHdrNbOfTxs,
                                        calculatedGrpHdrNbOfTxs));
            }


            return errors;
        }



}
