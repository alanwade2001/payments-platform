package com.acme.payments.initiation.internal.persistence;

import com.acme.payments.initiation.model.Initiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInitiationRepository extends JpaRepository<Initiation, Long> {
}
