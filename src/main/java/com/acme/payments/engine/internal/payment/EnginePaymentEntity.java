package com.acme.payments.engine.internal.payment;

import com.acme.payments.engine.EnginePaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "engine_payment",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_engine_payment_external_reference",
                columnNames = "external_reference"
        )
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class EnginePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "external_reference",
            nullable = false,
            length = 100)
    private String externalReference;

    @Column(
            name = "debtor_iban",
            nullable = false,
            length = 34)
    private String debtorIban;

    @Column(
            name = "creditor_iban",
            nullable = false,
            length = 34)
    private String creditorIban;

    @Column(
            name = "end_to_end_id",
            nullable = false,
            length = 35)
    private String endToEndId;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2)
    private BigDecimal amount;

    @Column(
            nullable = false,
            length = 3)
    private String currency;

    @Column(
            name = "execution_date",
            nullable = false)
    private LocalDate executionDate;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20)
    private EnginePaymentStatus status;
}