package com.acme.payments.initiation.internal.ingest.content;

import com.acme.payments.initiation.internal.ingest.Pain001CustomerIdResolver;
import com.acme.payments.initiation.internal.ingest.Pain001ImportException;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomerIdPresentRule implements Pain001ContentRule{

    @Override
    public List<String> validate(Document document) {
        try{
            Pain001CustomerIdResolver.resolve(document);
        } catch (Pain001ImportException x) {
            return List.of(x.getMessage());
        }

        return List.of();
    }
}
