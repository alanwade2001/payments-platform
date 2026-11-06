package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.SchemeId;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "execution_transaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_execution_transaction_instructed_transaction",
                        columnNames = "instructed_transaction_id"
                )
        },
        indexes = {
                @Index(
                        name = "idx_execution_transaction_ready",
                        columnList = "status, execution_date"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "instructed_transaction_id")
    private InstructedTransactionEntity instructedTransaction;

    @Column(
            name = "execution_date",
            nullable = false,
            updatable = false
    )
    private LocalDate executionDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private ExecutionStatus status;

    @Column(
            name = "scheme_id",
            nullable = false,
            length = 20
    )
    private SchemeId schemeId;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        var now = Instant.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public ExecutionTransactionId id() {
        return new ExecutionTransactionId(id + "");
    }
}
