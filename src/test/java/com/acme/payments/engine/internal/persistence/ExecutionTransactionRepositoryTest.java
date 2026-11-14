package com.acme.payments.engine.internal.persistence;

import com.acme.payments.PostgresqlTestConfiguration;
import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransactionStatus;
import com.acme.payments.engine.SchemeId;
import iso.std.iso._20022.tech.xsd.pacs_008_001.CreditTransferTransaction73;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostgresqlTestConfiguration.class)
class ExecutionTransactionRepositoryTest {

    private final String SCHEME = "SCT";
    private final SchemeId SCHEME_ID = new  SchemeId(SCHEME);


    @Autowired
    private ExecutionTransactionRepository repository;

    @Autowired
    private InstructedTransactionRepository instructedTransactionRepository;

    @Test
    void shouldFindExecutionTransactionUsingExecutionTransactionId() {

        ExecutionTransactionEntity entity =
                repository.saveAndFlush(newEntity(ExecutionStatus.READY));

        ExecutionTransactionId id =
                new ExecutionTransactionId(entity.getId().toString());

        var result = repository.findById(id);

        assertThat(result)
                .isPresent()
                .get()
                .extracting(ExecutionTransactionEntity::getId)
                .isEqualTo(entity.getId());
    }

    @Test
    void shouldReturnEmptyWhenExecutionTransactionIdIsNotNumeric() {

        ExecutionTransactionId id =
                new ExecutionTransactionId("unknown");

        var result = repository.findById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldUpdateStatusWhenExistingStatusMatches() {

        ExecutionTransactionEntity entity =
                repository.saveAndFlush(newEntity(ExecutionStatus.READY));

        int updated = repository.updateStatusById(
                entity.getId(),
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        repository.flush();

        assertThat(updated).isEqualTo(1);

        ExecutionTransactionEntity reloaded =
                repository.findById(entity.getId())
                        .orElseThrow();

        assertThat(reloaded.getStatus())
                .isEqualTo(ExecutionStatus.EXECUTING);
    }

    @Test
    void shouldNotUpdateStatusWhenExistingStatusDoesNotMatch() {

        ExecutionTransactionEntity entity =
                repository.saveAndFlush(
                        newEntity(ExecutionStatus.EXECUTED));

        int updated = repository.updateStatusById(
                entity.getId(),
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        repository.flush();

        assertThat(updated).isZero();

        ExecutionTransactionEntity reloaded =
                repository.findById(entity.getId())
                        .orElseThrow();

        assertThat(reloaded.getStatus())
                .isEqualTo(ExecutionStatus.EXECUTED);
    }

    @Test
    void shouldReturnZeroWhenUpdatingUnknownId() {

        int updated = repository.updateStatusById(
                Long.MAX_VALUE,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        assertThat(updated).isZero();
    }

    @Test
    void shouldReturnZeroWhenExecutionTransactionIdIsNotNumeric() {

        ExecutionTransactionId id =
                new ExecutionTransactionId("999");

        int updated = repository.updateStatusById(
                Long.parseLong(id.value()),
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        assertThat(updated).isZero();
    }

    @Test
    void shouldUpdateEntireBatchWhenStatusesMatch() {

        ExecutionTransactionEntity first =
                repository.save(newEntity(ExecutionStatus.READY));

        ExecutionTransactionEntity second =
                repository.save(newEntity(ExecutionStatus.READY));

        repository.flush();

        int updated = repository.updateStatusByEntityId(
                List.of(first.getId(), second.getId()),
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        repository.flush();

        assertThat(updated).isEqualTo(2);

        assertThat(repository.findById(first.getId()).orElseThrow().getStatus())
                .isEqualTo(ExecutionStatus.EXECUTING);

        assertThat(repository.findById(second.getId()).orElseThrow().getStatus())
                .isEqualTo(ExecutionStatus.EXECUTING);
    }

    @Test
    void shouldOnlyUpdateRowsWhoseExistingStatusMatches() {

        ExecutionTransactionEntity ready =
                repository.save(newEntity(ExecutionStatus.READY));

        ExecutionTransactionEntity executed =
                repository.save(newEntity(ExecutionStatus.EXECUTED));

        repository.flush();

        int updated = repository.updateStatusByEntityId(
                List.of(ready.getId(), executed.getId()),
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        repository.flush();

        assertThat(updated).isEqualTo(1);

        assertThat(repository.findById(ready.getId()).orElseThrow().getStatus())
                .isEqualTo(ExecutionStatus.EXECUTING);

        assertThat(repository.findById(executed.getId()).orElseThrow().getStatus())
                .isEqualTo(ExecutionStatus.EXECUTED);
    }

    @Test
    void shouldFindReadyTransactionsForSchemeDueOnExecutionDate() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);
        var schemeId = new SchemeId("SCT");

        ExecutionTransactionEntity transaction =
                repository.saveAndFlush(
                        newEntity(ExecutionStatus.READY,
                                schemeId,
                                executionDate));

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result)
                .extracting(ExecutionTransactionEntity::getId)
                .containsExactly(transaction.getId());
    }

    @Test
    void shouldFindReadyTransactionsDueBeforeExecutionDate() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);

        ExecutionTransactionEntity transaction =
                repository.saveAndFlush(
                        newEntity(ExecutionStatus.READY,
                                SCHEME_ID,
                                executionDate.minusDays(1)));

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result)
                .extracting(ExecutionTransactionEntity::getId)
                .containsExactly(transaction.getId());
    }

    @Test
    void shouldNotFindTransactionsDueAfterExecutionDate() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);

        repository.saveAndFlush(
                newEntity(
                        ExecutionStatus.READY,
                        SCHEME_ID,
                        executionDate.plusDays(1)));

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldNotFindTransactionsThatAreNotReady() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);

        repository.save(
                newEntity(
                        ExecutionStatus.EXECUTED,
                        SCHEME_ID,
                        executionDate));

        repository.save(
                newEntity(
                        ExecutionStatus.FAILED,
                        SCHEME_ID,
                        executionDate));

        repository.save(
                newEntity(
                        ExecutionStatus.EXECUTING,
                        SCHEME_ID,
                        executionDate));

        repository.flush();

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldNotFindReadyTransactionsForDifferentScheme() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);

        repository.saveAndFlush(
                newEntity(
                        ExecutionStatus.READY,
                        new SchemeId("OTHER"),
                        executionDate));

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindOnlyTransactionsEligibleForExport() {

        LocalDate executionDate = LocalDate.of(2026, 11, 14);

        ExecutionTransactionEntity yesterday =
                repository.save(
                        newEntity(
                                ExecutionStatus.READY,
                                SCHEME_ID,
                                executionDate.minusDays(1)));

        ExecutionTransactionEntity today =
                repository.save(
                        newEntity(
                                ExecutionStatus.READY,
                                SCHEME_ID,
                                executionDate));

        repository.save(
                newEntity(
                        ExecutionStatus.READY,
                        SCHEME_ID,
                        executionDate.plusDays(1)));

        repository.save(
                newEntity(
                        ExecutionStatus.EXECUTED,
                        SCHEME_ID,
                        executionDate));

        repository.save(
                newEntity(
                        ExecutionStatus.FAILED,
                        SCHEME_ID,
                        executionDate));

        repository.save(
                newEntity(
                        ExecutionStatus.READY,
                        new SchemeId("OTHER"),
                        executionDate));

        repository.flush();

        var result = repository.findExecutionForStatus(
                SCHEME_ID,
                ExecutionStatus.READY,
                executionDate);

        assertThat(result)
                .extracting(ExecutionTransactionEntity::getId)
                .containsExactly(
                        yesterday.getId(),
                        today.getId());
    }

    private ExecutionTransactionEntity newEntity(ExecutionStatus status, SchemeId schemeId, LocalDate executionDate) {
        InstructedTransactionEntity instructedTransaction = InstructedTransactionEntity
                .builder()
                .transactionId(UUID.randomUUID().toString())
                .cdtTrfTxInf(new CreditTransferTransaction73())
                .schemeId(schemeId)
                .status(InstructedTransactionStatus.ACCEPTED)
                .build();

        var saved = instructedTransactionRepository.save(instructedTransaction);

        ExecutionTransactionEntity entity = ExecutionTransactionEntity
                .builder()
                .executionDate(executionDate)
                .instructedTransaction(saved)
                .status(status)
                .schemeId(schemeId)
                .build();

        entity.setStatus(status);

        return entity;
    }

    private ExecutionTransactionEntity newEntity(
            ExecutionStatus status) {

        return newEntity(status, new SchemeId("SCT"), LocalDate.now());
    }
}