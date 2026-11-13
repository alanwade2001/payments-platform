package com.acme.payments.regression;

import com.acme.payments.PostgresqlTestConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Import({PostgresqlTestConfiguration.class, RegressionTestConfiguration.class})
public class CucumberSpringConfiguration {
}