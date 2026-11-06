package com.acme.payments.engine.internal.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructedTransactionRepository extends JpaRepository<InstructedTransactionEntity, Long> {
}
