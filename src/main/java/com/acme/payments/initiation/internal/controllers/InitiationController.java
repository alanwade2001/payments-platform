package com.acme.payments.initiation.internal.controllers;

import com.acme.payments.initiation.InitiationDetails;
import com.acme.payments.initiation.InitiationId;
import com.acme.payments.initiation.internal.ingest.Pain001ImportService;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<InitiationDetails> getInitiationDetails(@PathVariable InitiationId id) {
        Optional<InitiationDetails> result = importService.getInitiation( id );
        return ResponseEntity.of(result);
    }
}
