package com.acme.payments.reference;

import com.acme.payments.PostgresqlIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql("/reference-test-data.sql")
class CustomersIntegrationTest extends PostgresqlIntegrationTest {

    @Autowired
    private Customers customers;

    @Test
    void returnsCustomer() {
        Optional<CustomerDetails> result =
                customers.getCustomer(new CustomerId("1"));

        var customer = result.orElseThrow();

        assertThat(customer.customerId())
                .isEqualTo(new CustomerId("1"));

        assertThat(customer.name())
                .isEqualTo("ACME Ltd");

        assertThat(customer.status())
                .isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    void returnsSuspendedCustomer() {
        var result =
                customers.getCustomer(new CustomerId("2"));

        var customer = result.orElseThrow();

        assertThat(customer.status())
                .isEqualTo(CustomerStatus.SUSPENDED);
    }

    @Test
    void throwsWhenCustomerDoesNotExist() {
        CustomerId id = new CustomerId("999");

        assertThat(customers.getCustomer(id)).isEmpty();

    }
}