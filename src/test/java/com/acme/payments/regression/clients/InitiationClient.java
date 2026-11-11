package com.acme.payments.regression.clients;

import com.acme.payments.initiation.InitiationDetails;
import com.acme.payments.initiation.InitiationId;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InitiationClient {
    private final TestApiClient testApiClient;
    private final String base = "/api/initiation";
    public InitiationId initiatePayment(
            String xml) {
        return testApiClient.postXml(
                base,
                xml,
                InitiationId.class
        );
    }



    public Optional<InitiationDetails> getInitiationDetails(InitiationId id) {
        String local = "/%s";
        String path = base+local;
        String uri = String.format(path, id.value());

        try {
            var details = testApiClient.get(
                    uri,
                    InitiationDetails.class
            );

            return Optional.of(details);
        }catch (Exception e) {
            return Optional.empty();
        }
    }
}
