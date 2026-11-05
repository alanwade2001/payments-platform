package com.acme.payments.initiation.internal.ingest.content;

import iso.std.iso._20022.tech.xsd.pain_001_001.Document;

import java.util.List;

public interface Pain001ContentRule {

    List<String> validate(Document document);
}
