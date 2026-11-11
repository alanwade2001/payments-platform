package com.acme.payments;

import org.springframework.context.annotation.Import;

@Import(PostgresqlTestConfiguration.class)
public class PostgresqlIntegrationTest {
}
