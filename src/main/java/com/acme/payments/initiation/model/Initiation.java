package com.acme.payments.initiation.model;

import com.acme.payments.initiation.InitiationId;
import com.acme.payments.initiation.InitiationStatus;
import com.acme.payments.reference.CustomerId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "initiation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Initiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private CustomerId customerId;

    @Column(nullable = false)
    private String messageId;

    @Column(nullable = false)
    private Instant sourceCreatedAt;

    @Column(nullable = false)
    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InitiationStatus status;

    @OneToMany(
            mappedBy = "initiation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Builder
    private Initiation(
            CustomerId customerId,
            String messageId,
            Instant sourceCreatedAt,
            Instant receivedAt,
            InitiationStatus status) {

        this.customerId = customerId;
        this.messageId = messageId;
        this.sourceCreatedAt = sourceCreatedAt;
        this.receivedAt = receivedAt;
        this.status = status;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInitiation(this);
    }

    public InitiationId id() {
        if( this.id == null) return null;
        return new InitiationId(this.id + "");
    }
}
