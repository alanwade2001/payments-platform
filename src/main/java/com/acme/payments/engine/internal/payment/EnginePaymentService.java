package com.acme.payments.engine.internal.payment;

import com.acme.payments.engine.CreditTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnginePaymentService {

    private final EnginePaymentRepository repository;
    private final EnginePaymentMapper mapper;

    @Transactional
    public void receive(CreditTransfer creditTransfer) {

        var entity = mapper.toEntity(creditTransfer);

        repository.save(entity);
    }
}
