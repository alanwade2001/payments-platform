package com.acme.payments.reference.internal.product;

import com.acme.payments.reference.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductEntitlementRepository extends JpaRepository<ProductEntitlementEntity, Long> {
    Optional<ProductEntitlementEntity> findByCustomerIdAndProductCode(long customerId, ProductCode productCode);
}
