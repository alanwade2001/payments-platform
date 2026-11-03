package com.acme.payments.initiation.internal.submission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository
        extends JpaRepository<SubmissionEntity, Long> {
}
