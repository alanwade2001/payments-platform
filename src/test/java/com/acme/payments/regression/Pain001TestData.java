package com.acme.payments.regression;

import com.acme.payments.reference.CustomerDetails;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import jakarta.xml.bind.JAXBContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.transform.stream.StreamSource;
import java.io.FileReader;
import java.io.StringWriter;

@RequiredArgsConstructor
@Data
public class Pain001TestData {

    private final Document document;

    public static Pain001TestData validSct() {
        return new Pain001TestData(
                load("/features/pain001/valid-sct.xml")
        );
    }



    public Pain001TestData setDebtorIban(String iban) {

        document.getCstmrCdtTrfInitn()
                .getPmtInves()
                .getFirst()
                .getDbtrAcct()
                .getId()
                .setIBAN(iban);

        return this;
    }

    public Pain001TestData setEndToEnd( String endToEndId ) {

        this.getDocument()
                .getCstmrCdtTrfInitn()
                .getPmtInves()
                .getFirst()
                .getCdtTrfTxInves()
                .getFirst()
                .getPmtId()
                .setEndToEndId(endToEndId);

        return this;
    }

    public String getEndToEndId() {
        return this.getDocument()
                .getCstmrCdtTrfInitn()
                .getPmtInves()
                .getFirst()
                .getCdtTrfTxInves()
                .getFirst()
                .getPmtId()
                .getEndToEndId();
    }



    public Pain001TestData setCustomerDetails(CustomerDetails customerDetails) {
        document.getCstmrCdtTrfInitn()
                .getGrpHdr()
                .getInitgPty()
                .getId()
                .getOrgId()
                .getOthrs()
                .getFirst()
                .setId(customerDetails.customerId().value());

        return this;
    }

    @SneakyThrows
    public static String xml(Document document) {
        var context = JAXBContext.newInstance(Document.class);
        var writer = new StringWriter();
        context.createMarshaller().marshal(document, writer);

        return writer.toString();
    }

    @SneakyThrows
    public String xml() {
        var context = JAXBContext.newInstance(Document.class);
        var writer = new StringWriter();
        context.createMarshaller().marshal(document, writer);

        return writer.toString();
    }

    @SneakyThrows
    public static Document load( String resource ) {
        var context = JAXBContext.newInstance(Document.class);

        try (var input = Pain001TestData.class.getResourceAsStream(resource)) {

            if (input == null) {
                throw new IllegalArgumentException(
                        "Test resource not found: " + resource
                );
            }

            return context.createUnmarshaller().unmarshal(new StreamSource(input), Document.class).getValue();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load test resource: " + resource,
                    e
            );
        }
    }
}
