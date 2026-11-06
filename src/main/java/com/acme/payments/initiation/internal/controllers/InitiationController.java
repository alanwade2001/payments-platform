package com.acme.payments.initiation.internal.controllers;

import com.acme.payments.initiation.InitiationId;
import com.acme.payments.initiation.internal.ingest.Pain001ImportService;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/initiation")
@RequiredArgsConstructor
public class InitiationController {

    private final Pain001ImportService importService;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<InitiationId> initiate(@RequestBody String content) {

        var id = importService.save(content);

        return ResponseEntity.accepted().body(id);
    }
}
