package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.internal.ingest.business.Pain001BusinessRule;
import com.acme.payments.initiation.internal.ingest.content.Pain001ContentRule;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Pain001BusinessValidator {

    private final List<Pain001BusinessRule> rules;


    public void validate(Document document) {

        var errors = rules.stream()
                .flatMap(rule -> rule.validate(document).stream())
                .toList();

        if (!errors.isEmpty()) {
            throw new Pain001ImportException(errors.toString());
        }
    }
}

