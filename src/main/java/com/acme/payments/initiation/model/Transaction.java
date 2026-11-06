package com.acme.payments.initiation.model;

import iso.std.iso._20022.tech.xsd.pain_001_001.CreditTransferTransaction61;
import iso.std.iso._20022.tech.xsd.pain_001_001.PaymentInstruction44;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cdt_trf_tx_inf", nullable = false, columnDefinition = "jsonb")
    private CreditTransferTransaction61 cdtTrfTxInf;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Builder
    private Transaction(
            String endToEndId,
            BigDecimal amount,
            String currency,
            String creditorName,
            String creditorIban,
            CreditTransferTransaction61 cdtTrfTxInf) {

        this.endToEndId = endToEndId;
        this.amount = amount;
        this.currency = currency;
        this.creditorName = creditorName;
        this.creditorIban = creditorIban;
        this.cdtTrfTxInf = cdtTrfTxInf;
    }

    void setPayment(Payment payment) {
        this.payment = payment;
    }
}
