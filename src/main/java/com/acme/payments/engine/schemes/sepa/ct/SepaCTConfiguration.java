package com.acme.payments.engine.schemes.sepa.ct;

import iso.std.iso._20022.tech.xsd.pacs_008_001.Document;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(SepaCTProperties.class)
public class SepaCTConfiguration {
    @Bean
    Jaxb2Marshaller pacs008Marshaller() {

        var marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(Document.class);

        return marshaller;
    }

    @Bean
    Schema pacs008Schema() throws IOException, SAXException {
        var r = new ClassPathResource("xsd/pacs.008.001.14.xsd");
        return SchemaFactory.newDefaultInstance().newSchema(r.getURL());
    }
}
