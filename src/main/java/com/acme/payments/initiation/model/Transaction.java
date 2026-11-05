package com.acme.payments.initiation.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String endToEndId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String creditorName;

    @Column(nullable = false)
    private String creditorIban;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Builder
    private Transaction(
            String endToEndId,
            BigDecimal amount,
            String currency,
            String creditorName,
            String creditorIban) {

        this.endToEndId = endToEndId;
        this.amount = amount;
        this.currency = currency;
        this.creditorName = creditorName;
        this.creditorIban = creditorIban;
    }

    void setPayment(Payment payment) {
        this.payment = payment;
    }
}
