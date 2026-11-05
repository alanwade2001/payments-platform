package com.acme.payments.initiation.internal.ingest;

import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

@Configuration
public class Pain001Configuration {
    @Bean
    Jaxb2Marshaller pain001Marshaller() {

        var marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(Document.class);

        return marshaller;
    }

    @Bean
    Schema pain001Schema() throws IOException, SAXException {
        var r = new ClassPathResource("xsd/pain.001.001.12.xsd");
        return SchemaFactory.newDefaultInstance().newSchema(r.getURL());
    }
}
