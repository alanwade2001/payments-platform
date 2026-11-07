package com.acme.payments.reference.internal.customer;

import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    public CustomerId id() {
        if (id == null) {return null;}
        return new CustomerId(id + "");
    }
}