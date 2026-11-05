package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.internal.ingest.content.Pain001ContentRule;
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
public class Pain001SchemaValidator {

    private final Schema pain001Schema;

    public void validate(Source source) {
        try {
            Validator validator = pain001Schema.newValidator();
            validator.validate(source);
        } catch (SAXException | IOException e) {
            throw new Pain001ImportException(
                    "pain.001 failed XML schema validation",
                    e
            );
        }
    }
}
