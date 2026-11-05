package com.acme.payments.initiation.internal.ingest.content;

import com.acme.payments.initiation.internal.ingest.Pain001Calculations;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ControlSumRule implements Pain001ContentRule {


        @Override
        public List<String> validate(Document document) {

            List<String> errors = new ArrayList<>();

            for (var pmtInf : document.getCstmrCdtTrfInitn().getPmtInves()) {

                var pmtInfCalculated = Pain001Calculations.controlSum(pmtInf);

                if (pmtInf.getCtrlSum() != null
                        && pmtInf.getCtrlSum().compareTo(pmtInfCalculated) != 0) {

                    errors.add(
                            "pmt inf ctrlSum: %s != calculated: %s"
                                    .formatted(
                                            pmtInf.getCtrlSum(),
                                            pmtInfCalculated));
                }
            }

            var ctrlSum = document.getCstmrCdtTrfInitn()
                    .getGrpHdr()
                    .getCtrlSum();

            var calculated = Pain001Calculations.controlSum(document);

            if (ctrlSum.compareTo(calculated) != 0) {
                errors.add(
                        "grp hdr ctrlSum: %s != calculated: %s"
                                .formatted(ctrlSum, calculated));
            }


            return errors;
        }

}
