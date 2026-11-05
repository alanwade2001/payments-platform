package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.internal.persistence.InitiationRepository;
import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.model.InitiationId;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Pain001ImportService {
    private final InitiationRepository initiationRepository;
    private final Pain001AcceptanceService pain001AcceptanceService;
    private final Pain001Mapper pain001Mapper;

    public InitiationId save(String content) {
        Document document = pain001AcceptanceService.importXml(content);
        Initiation initiation = pain001Mapper.map(document);
        return initiationRepository.save(initiation);
    }
}
