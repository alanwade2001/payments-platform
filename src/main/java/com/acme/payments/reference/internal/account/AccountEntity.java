package com.acme.payments.reference.internal.account;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.AccountId;
import com.acme.payments.reference.AccountStatus;
import com.acme.payments.reference.CustomerId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private CustomerId customerId;

    @Column(nullable = false, unique = true)
    private Iban iban;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    public AccountId id() {
        if (id == null) {return null;}
        return new AccountId(this.id + "");
    }
}
