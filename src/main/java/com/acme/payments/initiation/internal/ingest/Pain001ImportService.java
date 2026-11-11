package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.InitiationDetails;
import com.acme.payments.initiation.InitiationImported;
import com.acme.payments.initiation.internal.persistence.InitiationRepository;
import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.InitiationId;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Pain001ImportService {
    private final InitiationRepository initiationRepository;
    private final Pain001Mapper pain001Mapper;
    private final Pain001SchemaValidator pain001SchemaValidator;
    private final Pain001ContentValidator pain001ContentValidator;
    private final Pain001BusinessValidator pain001BusinessValidator;
    private final Jaxb2Marshaller pain001Marshaller;
    private final InitiationImportedPublisher initiationImportedPublisher;

    public InitiationId save(String content) {
        validateSchema(content);

        var document = unmarshal(content);

        pain001ContentValidator.validate(document);

        pain001BusinessValidator.validate(document);

        Initiation initiation = pain001Mapper.map(document);

        InitiationId id = initiationRepository.save(initiation);

        initiationImportedPublisher.publish(id);

        return id;
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

    public Optional<InitiationDetails> getInitiation(InitiationId id) {
        return initiationRepository.findById(id).map(i->{
            return new InitiationDetails(i.id(), i.getMessageId(), i.getStatus(), i.getReceivedAt());
        });
    }
}
