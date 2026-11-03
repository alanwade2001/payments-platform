package com.acme.payments.engine.internal.payment;

import org.springframework.data.jpa.repository.JpaRepository;

interface EnginePaymentRepository
        extends JpaRepository<EnginePaymentEntity, Long> {
}
