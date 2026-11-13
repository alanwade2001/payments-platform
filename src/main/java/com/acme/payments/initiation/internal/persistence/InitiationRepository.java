package com.acme.payments.initiation.internal.persistence;

import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.InitiationId;
import jakarta.persistence.Convert;

import java.util.Optional;

public interface InitiationRepository {
    InitiationId save(Initiation initiation);

    @Convert(converter = InitiationIdConverter.class)
    Optional<Initiation> findById(InitiationId id);
}
