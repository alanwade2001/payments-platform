package com.acme.payments.initiation.internal.ingest.business;

import iso.std.iso._20022.tech.xsd.pain_001_001.Document;

import java.util.List;

public class DailyFileLimitRule implements Pain001BusinessRule {
    @Override
    public List<String> validate(Document document) {
        return List.of();
    }
}
