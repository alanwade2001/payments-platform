package com.acme.payments.reference;

import org.springframework.context.annotation.Import;

@Import(PostgresqlTestConfiguration.class)
public class PostgresqlIntegrationTest {
}
