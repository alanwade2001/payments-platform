package com.acme.payments.initiation.internal.ingest;

import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class Pain001AcceptanceService {

    private final Pain001SchemaValidator pain001SchemaValidator;
    private final Pain001ContentValidator pain001ContentValidator;
    private final Pain001BusinessValidator pain001BusinessValidator;
    private final Jaxb2Marshaller pain001Marshaller;

    public Document importXml(String content) {

        validateSchema(content);

        var document = unmarshal(content);

        pain001ContentValidator.validate(document);

        pain001BusinessValidator.validate(document);

        return document;
    }

    private void validateSchema(String content) {

        pain001SchemaValidator.validate(
                new StreamSource(new StringReader(content)));
    }

    private Document unmarshal(String content) {

        try {
            return (Document) pain001Marshaller.unmarshal(
                    new StreamSource(new StringReader(content)));

        } catch (XmlMappingException exception) {
            throw new Pain001ImportException(
                    "pain.001 failed XML mapping",
                    exception);
        }
    }
}