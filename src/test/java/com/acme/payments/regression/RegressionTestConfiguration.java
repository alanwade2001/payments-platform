package com.acme.payments.regression;

import com.acme.payments.regression.clients.EngineAdmin;
import com.acme.payments.regression.clients.InitiationClient;
import com.acme.payments.regression.clients.ReferenceAdmin;
import com.acme.payments.regression.clients.TestApiClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@TestConfiguration
class RegressionTestConfiguration {

    @Bean
    TestApiClient testApiClient(MockMvc mockMvc, ObjectMapper objectMapper) {
        return new TestApiClient(mockMvc, objectMapper);
    }

    @Bean
    EngineAdmin engineAdmin(TestApiClient testApiClient) {
        return new EngineAdmin(testApiClient);
    }

    @Bean
    ReferenceAdmin referenceAdmin(TestApiClient testApiClient) {
        return new ReferenceAdmin(testApiClient);
    }

    @Bean
    InitiationClient initiationClient(TestApiClient testApiClient) {
        return new InitiationClient(testApiClient);
    }
}
