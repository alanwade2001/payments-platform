package com.acme.payments.engine.schemes.sepa.ct;

import iso.std.iso._20022.tech.xsd.pacs_008_001.Document;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
@Component
public class SepaCTMarshaller {

    private final Jaxb2Marshaller pacs008Marshaller;

    @SneakyThrows
    public byte[] marshal(Document document) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pacs008Marshaller.createMarshaller().marshal(document, stream);
        return stream.toByteArray();
    }
}
