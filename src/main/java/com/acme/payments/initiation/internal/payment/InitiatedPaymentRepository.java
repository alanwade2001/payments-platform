package com.acme.payments.initiation.internal.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface InitiatedPaymentRepository
        extends JpaRepository<InitiatedPaymentEntity, Long> {
}

