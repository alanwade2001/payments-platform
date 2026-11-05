package com.acme.payments.initiation.internal.payment;

import com.acme.payments.initiation.InitiateCreditTransfer;
import com.acme.payments.initiation.PaymentId;
import com.acme.payments.initiation.SubmissionId;
import com.acme.payments.reference.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitiatedPaymentService {

    private final InitiatedPaymentRepository repository;

    public PaymentId create(
            SubmissionId submissionId,
            AccountId debtorAccountId,
            InitiateCreditTransfer request) {

        var entity = InitiatedPaymentEntity.builder()
                .submissionId(Long.parseLong(submissionId.value()))
                .debtorAccountId(Long.parseLong(debtorAccountId.value()))
                .debtorIban(request.debtorIban().value())
                .creditorIban(request.creditorIban().value())
                .endToEndId(request.endToEndId())
                .amount(request.amount())
                .currency(request.currency().getCurrencyCode())
                .executionDate(request.requestedExecutionDate())
                .build();

        var saved = repository.save(entity);

        return new PaymentId(saved.getId());
    }
}