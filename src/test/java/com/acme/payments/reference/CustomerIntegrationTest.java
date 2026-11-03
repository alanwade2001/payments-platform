package com.acme.payments.reference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
        CustomerDetails customer =
                customers.getCustomer(new CustomerId(1));

        assertThat(customer.customerId())
                .isEqualTo(new CustomerId(1));

        assertThat(customer.name())
                .isEqualTo("ACME Ltd");

        assertThat(customer.status())
                .isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    void returnsSuspendedCustomer() {
        CustomerDetails customer =
                customers.getCustomer(new CustomerId(3));

        assertThat(customer.status())
                .isEqualTo(CustomerStatus.SUSPENDED);
    }

    @Test
    void throwsWhenCustomerDoesNotExist() {
        CustomerId id = new CustomerId(999);

        assertThatThrownBy(() -> customers.getCustomer(id))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}