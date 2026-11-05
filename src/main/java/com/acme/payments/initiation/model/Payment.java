package com.acme.payments.initiation.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentInformationId;

    @Column(nullable = false)
    private String debtorName;

    @Column(nullable = false)
    private String debtorIban;

    @Column(nullable = false)
    private LocalDate requestedExecutionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiation_id", nullable = false)
    private Initiation initiation;

    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @Builder
    private Payment(
            String paymentInformationId,
            String debtorName,
            String debtorIban,
            LocalDate requestedExecutionDate) {

        this.paymentInformationId = paymentInformationId;
        this.debtorName = debtorName;
        this.debtorIban = debtorIban;
        this.requestedExecutionDate = requestedExecutionDate;
    }

    void setInitiation(Initiation initiation) {
        this.initiation = initiation;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setPayment(this);
    }
}
