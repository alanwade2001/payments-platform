package com.acme.payments.initiation.internal.persistence;

import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.InitiationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InitiationRepositoryAdapter implements InitiationRepository {
    private final JpaInitiationRepository jpaInitiationRepository;

    @Override
    public InitiationId save(Initiation initiation) {
        var saved = jpaInitiationRepository.save(initiation);
        return saved.id();
    }

    @Override
    public Optional<Initiation> findById(InitiationId id) {
        Long lid = Long.parseLong(id.value());
        return jpaInitiationRepository.findById(lid);
    }
}
