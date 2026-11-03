package com.acme.payments.initiation.internal.submission;

import com.acme.payments.initiation.SubmissionId;
import com.acme.payments.initiation.SubmissionStatus;
import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.ProductCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class SubmissionService {
    private final SubmissionRepository repository;

    public SubmissionId create(
            CustomerId customerId,
            ProductCode productCode) {

        var entity = SubmissionEntity.builder()
                .customerId(customerId.value())
                .productCode(productCode)
                .status(SubmissionStatus.RECEIVED)
                .createdAt(Instant.now())
                .build();

        var saved = repository.save(entity);

        return new SubmissionId(saved.getId());
    }

    public void accept(SubmissionId submissionId) {
        var submission = getSubmission(submissionId);
        submission.accept();
    }

    public void reject(SubmissionId submissionId) {
        var submission = getSubmission(submissionId);
        submission.reject();
    }

    private SubmissionEntity getSubmission(SubmissionId submissionId) {
        return repository.findById(submissionId.value())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Submission not found: " + submissionId));
    }
}
