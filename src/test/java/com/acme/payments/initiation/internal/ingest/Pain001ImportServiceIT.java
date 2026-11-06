package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.internal.persistence.InitiationRepository;
import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.InitiationId;
import com.acme.payments.reference.PostgresqlIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Sql("/reference-test-data.sql")
class Pain001ImportServiceIT extends PostgresqlIntegrationTest {

    @Autowired
    private Pain001ImportService pain001ImportService;

    @Autowired
    private InitiationRepository initiationRepository;

    @Transactional
    @Test
    void importsValidPain001AndReturnsPersistedInitiationId() throws Exception {
        // given

            String xml = Files.readString(
                    Path.of("src/test/resources/pain001/valid-pain001.xml")
            );

            // when
            InitiationId initiationId = pain001ImportService.save(xml);

            // then
            assertThat(initiationId).isNotNull();

            Initiation initiation = initiationRepository
                    .findById(initiationId)
                    .orElseThrow();

            assertThat(initiation.getId() + "").isEqualTo(initiationId.value());

            assertThat( initiation.getPayments()).hasSize(1);

            assertThat( initiation.getPayments().getFirst().getId()).isPositive();

            //Thread.sleep(Duration.ofSeconds(2));
    }
}