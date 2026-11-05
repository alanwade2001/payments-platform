package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.reference.CustomerId;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.springframework.stereotype.Component;

@Component
public class Pain001CustomerIdResolver {

    private Pain001CustomerIdResolver() {
    }

    public static CustomerId resolve(Document document) {

        var id = document.getCstmrCdtTrfInitn()
                .getGrpHdr()
                .getInitgPty()
                .getId();

        if (id.getOrgId() != null
                && !id.getOrgId().getOthrs().isEmpty()) {

            return new CustomerId(
                    id.getOrgId()
                            .getOthrs()
                            .getFirst()
                            .getId());
        }

        if (id.getPrvtId() != null
                && !id.getPrvtId().getOthrs().isEmpty()) {

            return new CustomerId(
                    id.getPrvtId()
                            .getOthrs()
                            .getFirst()
                            .getId());
        }

        throw new Pain001ImportException(
                "initiating party customerId is missing");
    }
}


