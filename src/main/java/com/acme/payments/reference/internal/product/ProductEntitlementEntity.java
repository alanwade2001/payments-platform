package com.acme.payments.reference.internal.product;

import com.acme.payments.reference.CustomerId;
import com.acme.payments.reference.EntitlementStatus;
import com.acme.payments.reference.ProductCode;
import com.acme.payments.reference.ProductEntitlementId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_entitlement",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"customer_id", "product_code"}
        )
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductEntitlementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private CustomerId customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_code", nullable = false)
    private ProductCode productCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntitlementStatus status;

    public ProductEntitlementId id() {
        if (id == null) {return null;}
        return new ProductEntitlementId(id + "");
    }
}
