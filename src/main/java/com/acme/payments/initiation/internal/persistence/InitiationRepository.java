package com.acme.payments.initiation.internal.persistence;

import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.model.InitiationId;

import java.util.Optional;

public interface InitiationRepository {
    InitiationId save(Initiation initiation);

    Optional<Initiation> findById(InitiationId id);
}
