package com.acme.payments.engine.internal;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.SchemeId;
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
    private Scheme scheme;

    @Mock
    private ExecutionStatusService executionStatusService;

    private ExecutionService executionService;

    @BeforeEach
    void setUp() {
        executionService = new ExecutionService(
                schemeRegistry,
                executionStatusService);
    }

    @Test
    void shouldDoNothingWhenThereAreNoTransactions() {
        executionService.execute(
                mock(SchemeId.class),
                List.of());

        verifyNoInteractions(
                schemeRegistry,
                scheme,
                executionStatusService);
    }

    @Test
    void shouldExecuteEntireBatchSuccessfully() {
        SchemeId schemeId = mock(SchemeId.class);

        ExecutionTransaction transaction1 =
                mock(ExecutionTransaction.class);

        ExecutionTransaction transaction2 =
                mock(ExecutionTransaction.class);

        List<ExecutionTransaction> transactions =
                List.of(transaction1, transaction2);

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        executionService.execute(
                schemeId,
                transactions);

        InOrder inOrder = inOrder(
                executionStatusService,
                schemeRegistry,
                scheme);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        inOrder.verify(schemeRegistry)
                .get(schemeId);

        inOrder.verify(scheme)
                .execute(transactions);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);

        verify(executionStatusService, never())
                .markAs(
                        transactions,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        verifyNoMoreInteractions(
                executionStatusService,
                schemeRegistry,
                scheme);
    }

    @Test
    void shouldFailWithoutExecutingSchemeWhenEntireBatchCannotBeMarkedExecuting() {
        SchemeId schemeId = mock(SchemeId.class);

        List<ExecutionTransaction> transactions =
                transactions();

        RuntimeException transitionFailure =
                new RuntimeException(
                        "Unable to mark entire batch as EXECUTING");

        doThrow(transitionFailure)
                .when(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        assertSame(
                transitionFailure,
                thrown);

        verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        verifyNoInteractions(
                schemeRegistry,
                scheme);

        verifyNoMoreInteractions(
                executionStatusService);
    }



    @Test
    void shouldMarkEntireBatchFailedWhenSchemeExecutionFails() {
        SchemeId schemeId = mock(SchemeId.class);

        List<ExecutionTransaction> transactions =
                transactions();

        RuntimeException schemeFailure =
                new RuntimeException(
                        "SEPA CT execution failed");

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        doThrow(schemeFailure)
                .when(scheme)
                .execute(transactions);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        assertSame(
                schemeFailure,
                thrown);

        InOrder inOrder = inOrder(
                executionStatusService,
                schemeRegistry,
                scheme);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        inOrder.verify(schemeRegistry)
                .get(schemeId);

        inOrder.verify(scheme)
                .execute(transactions);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        verify(executionStatusService, never())
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);

        verifyNoMoreInteractions(
                executionStatusService,
                schemeRegistry,
                scheme);
    }

    @Test
    void shouldFailWhenEntireBatchCannotBeMarkedFailed() {

        SchemeId schemeId = mock(SchemeId.class);

        List<ExecutionTransaction> transactions =
                transactions();

        RuntimeException schemeFailure =
                new RuntimeException(
                        "SEPA CT execution failed");

        RuntimeException statusFailure =
                new RuntimeException(
                        "Unable to mark entire batch as FAILED");

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        // READY -> EXECUTING succeeds
        doNothing()
                .when(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        // Scheme execution fails
        doThrow(schemeFailure)
                .when(scheme)
                .execute(transactions);

        // EXECUTING -> FAILED also fails
        doThrow(statusFailure)
                .when(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        assertSame(statusFailure, thrown);

        InOrder inOrder = inOrder(
                executionStatusService,
                schemeRegistry,
                scheme);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        inOrder.verify(schemeRegistry)
                .get(schemeId);

        inOrder.verify(scheme)
                .execute(transactions);

        inOrder.verify(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.EXECUTING);

        verify(executionStatusService, never())
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);
    }

    @Test
    void shouldFailWhenEntireBatchCannotBeMarkedExecuted() {

        SchemeId schemeId = mock(SchemeId.class);
        List<ExecutionTransaction> transactions = transactions();

        RuntimeException transitionFailure =
                new RuntimeException(
                        "Unable to mark entire batch as EXECUTED");

        when(schemeRegistry.get(schemeId))
                .thenReturn(scheme);

        // First transition succeeds
        doNothing()
                .when(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTING,
                        ExecutionStatus.READY);

        // Final transition fails
        doThrow(transitionFailure)
                .when(executionStatusService)
                .markAs(
                        transactions,
                        ExecutionStatus.EXECUTED,
                        ExecutionStatus.EXECUTING);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionService.execute(
                        schemeId,
                        transactions));

        assertSame(transitionFailure, thrown);

        verify(executionStatusService).markAs(
                transactions,
                ExecutionStatus.EXECUTING,
                ExecutionStatus.READY);

        verify(scheme).execute(transactions);

        verify(executionStatusService).markAs(
                transactions,
                ExecutionStatus.EXECUTED,
                ExecutionStatus.EXECUTING);

        verify(executionStatusService).markAs(
                transactions,
                ExecutionStatus.FAILED,
                ExecutionStatus.EXECUTING);
    }

    private List<ExecutionTransaction> transactions() {
        return List.of(
                mock(ExecutionTransaction.class),
                mock(ExecutionTransaction.class));
    }
}