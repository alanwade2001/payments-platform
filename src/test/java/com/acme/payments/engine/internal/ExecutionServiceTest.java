package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.SchemeId;
import com.acme.payments.engine.internal.persistence.ExecutionTransactionRepository;
import com.acme.payments.engine.schemes.Scheme;
import com.acme.payments.engine.schemes.SchemeRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionServiceTest {

    @Mock
    private SchemeRegistry schemeRegistry;

    @Mock
    private ExecutionTransactionRepository transactionRepository;

    @Mock
    private Scheme scheme;

    private ExecutionService executionService;

    @BeforeEach
    void setUp() {
        executionService = new ExecutionService(
                schemeRegistry,
                transactionRepository);
    }

    @Test
    void shouldDoNothingWhenThereAreNoTransactions() {

        executionService.execute(
                mock(SchemeId.class),
                List.of());

        verifyNoInteractions(
                schemeRegistry,
                transactionRepository,
                scheme);
    }

    @Test
    void shouldExecuteEntireBatchSuccessfully() {

        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransactionId id1 = mock(ExecutionTransactionId.class);
        ExecutionTransactionId id2 = mock(ExecutionTransactionId.class);

        ExecutionTransaction tx1 = mock(ExecutionTransaction.class);
        ExecutionTransaction tx2 = mock(ExecutionTransaction.class);

        when(tx1.id()).thenReturn(id1);
        when(tx2.id()).thenReturn(id2);

        List<ExecutionTransaction> transactions = List.of(tx1, tx2);
        List<ExecutionTransactionId> transactionIds = List.of(id1, id2);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY))
                .thenReturn(2);

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTED,
                ExecutionStatus.EXECUTING))
                .thenReturn(2);

        executionService.execute(
                schemeId,
                transactions);

        InOrder inOrder = inOrder(
                transactionRepository,
                schemeRegistry,
                scheme);

        inOrder.verify(transactionRepository)
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        inOrder.verify(schemeRegistry)
                .get(schemeId);

        inOrder.verify(scheme)
                .execute(transactions);

        inOrder.verify(transactionRepository)
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);

        verify(transactionRepository, never())
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);
    }

    @Test
    void shouldFailWithoutExecutingSchemeWhenEntireBatchCannotBeMarkedExecuting() {

        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransactionId id1 = mock(ExecutionTransactionId.class);
        ExecutionTransactionId id2 = mock(ExecutionTransactionId.class);

        ExecutionTransaction tx1 = mock(ExecutionTransaction.class);
        ExecutionTransaction tx2 = mock(ExecutionTransaction.class);

        when(tx1.id()).thenReturn(id1);
        when(tx2.id()).thenReturn(id2);

        List<ExecutionTransaction> transactions = List.of(tx1, tx2);
        List<ExecutionTransactionId> transactionIds = List.of(id1, id2);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY))
                .thenReturn(1);

        assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        verifyNoInteractions(schemeRegistry);
        verifyNoInteractions(scheme);

        verify(transactionRepository, never())
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);

        verify(transactionRepository, never())
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);
    }


    @Test
    void shouldFailWhenEntireBatchCannotBeMarkedExecuted() {

        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransactionId id1 = mock(ExecutionTransactionId.class);
        ExecutionTransactionId id2 = mock(ExecutionTransactionId.class);

        ExecutionTransaction tx1 = mock(ExecutionTransaction.class);
        ExecutionTransaction tx2 = mock(ExecutionTransaction.class);

        when(tx1.id()).thenReturn(id1);
        when(tx2.id()).thenReturn(id2);

        List<ExecutionTransaction> transactions = List.of(tx1, tx2);
        List<ExecutionTransactionId> transactionIds = List.of(id1, id2);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY))
                .thenReturn(2);

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTED,
                ExecutionStatus.EXECUTING))
                .thenReturn(1);

        assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        verify(scheme).execute(transactions);
    }

    @Test
    void shouldMarkEntireBatchFailedWhenSchemeExecutionFails() {
        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransactionId id1 = mock(ExecutionTransactionId.class);
        ExecutionTransactionId id2 = mock(ExecutionTransactionId.class);

        ExecutionTransaction tx1 = mock(ExecutionTransaction.class);
        ExecutionTransaction tx2 = mock(ExecutionTransaction.class);

        when(tx1.id()).thenReturn(id1);
        when(tx2.id()).thenReturn(id2);

        List<ExecutionTransaction> transactions = List.of(tx1, tx2);
        List<ExecutionTransactionId> transactionIds = List.of(id1, id2);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY))
                .thenReturn(2);

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        RuntimeException schemeFailure =
                new RuntimeException("SEPA CT execution failed");

        doThrow(schemeFailure)
                .when(scheme)
                .execute(transactions);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.FAILED,
                ExecutionStatus.EXECUTING))
                .thenReturn(2);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        assertSame(schemeFailure, thrown);

        InOrder inOrder = inOrder(
                transactionRepository,
                schemeRegistry,
                scheme);

        inOrder.verify(transactionRepository)
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        inOrder.verify(schemeRegistry)
                .get(schemeId);

        inOrder.verify(scheme)
                .execute(transactions);

        inOrder.verify(transactionRepository)
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        verify(transactionRepository, never())
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);
    }

    @Test
    void shouldFailWhenEntireBatchCannotBeMarkedFailed() {
        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransactionId id1 = mock(ExecutionTransactionId.class);
        ExecutionTransactionId id2 = mock(ExecutionTransactionId.class);

        ExecutionTransaction tx1 = mock(ExecutionTransaction.class);
        ExecutionTransaction tx2 = mock(ExecutionTransaction.class);

        when(tx1.id()).thenReturn(id1);
        when(tx2.id()).thenReturn(id2);

        List<ExecutionTransaction> transactions = List.of(tx1, tx2);
        List<ExecutionTransactionId> transactionIds = List.of(id1, id2);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY))
                .thenReturn(2);

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        RuntimeException schemeFailure =
                new RuntimeException("SEPA CT execution failed");

        doThrow(schemeFailure)
                .when(scheme)
                .execute(transactions);

        when(transactionRepository.updateStatusById(
                transactionIds,
                ExecutionStatus.FAILED,
                ExecutionStatus.EXECUTING))
                .thenReturn(1);

        assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        verify(transactionRepository)
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        verify(transactionRepository, never())
                .updateStatusById(
                        transactionIds,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);
    }
}