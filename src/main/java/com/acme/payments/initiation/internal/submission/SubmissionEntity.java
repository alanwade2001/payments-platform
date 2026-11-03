package com.acme.payments.initiation.internal.submission;

import com.acme.payments.initiation.SubmissionStatus;
import com.acme.payments.reference.ProductCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "submission")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_code", nullable = false, length = 50)
    private ProductCode productCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    void accept() {
        status = SubmissionStatus.ACCEPTED;
    }

    void reject() {
        status = SubmissionStatus.REJECTED;
    }
}
