package com.acme.payments.engine.internal.persistence;


import com.acme.payments.engine.InstructedTransactionId;
import com.acme.payments.engine.InstructedTransactionStatus;
import com.acme.payments.engine.SchemeId;
import iso.std.iso._20022.tech.xsd.pacs_008_001.CreditTransferTransaction73;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(
        name = "instructed_transaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_instructed_transaction_instruction_id",
                        columnNames = "instruction_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructedTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, updatable = false)
    private String transactionId;

    @Column(name = "scheme_id", nullable = false, updatable = false, length = 50)
    private SchemeId schemeId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "instruction", nullable = false, updatable = false, columnDefinition = "jsonb")
    private CreditTransferTransaction73 cdtTrfTxInf;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InstructedTransactionStatus status;

    @Column(name = "rejection_source", length = 50)
    private String rejectionSource;

    @Column(name = "rejection_code", length = 100)
    private String rejectionCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public InstructedTransactionId id() {
        return new InstructedTransactionId(id + "");
    }
}
