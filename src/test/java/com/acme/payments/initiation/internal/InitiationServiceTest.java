package com.acme.payments.initiation.internal;

import com.acme.payments.initiation.*;
import com.acme.payments.initiation.internal.submission.SubmissionRepository;
import com.acme.payments.initiation.internal.submission.SubmissionService;
import com.acme.payments.reference.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({
        InitiationIntegrationTest.TestConfig.class
})
@Sql("/reference-test-data.sql")
class InitiationIntegrationTest extends PostgresqlIntegrationTest{

    private static final CustomerId CUSTOMER_ID = new CustomerId(1);
    private static final CustomerId SUSPENDED_CUSTOMER_ID = new CustomerId(2);
    private static final CustomerId BLOCKED_ACCOUNT_CUSTOMER_ID = new CustomerId(3);
    private static final CustomerId BLOCKED_PRODUCT_CUSTOMER_ID = new CustomerId(4);


    private static final AccountId ACCOUNT_ID = new AccountId(10);
    private static final SubmissionId SUBMISSION_ID = new SubmissionId(100);

    private static final Iban DEBTOR_IBAN =
            new Iban("IE29AIBK93115212345678");

    private static final Iban CREDITOR_IBAN =
            new Iban("DE89370400440532013000");

    private static final Iban BLOCKED_ACCOUNT_IBAN = new Iban("IE64BOFI90001712345678");

    private static final Iban BLOCKED_PRODUCT_IBAN = new Iban("IE12BOFI90001712345679");

    @TestConfiguration
    static class TestConfig {

        @Bean
        PaymentInitiatedTestListener paymentInitiatedTestListener() {
            return new PaymentInitiatedTestListener();
        }
    }

    static class PaymentInitiatedTestListener {

        private final List<PaymentInitiated> events =
                new ArrayList<>();

        @EventListener
        void on(PaymentInitiated event) {
            events.add(event);
        }

        List<PaymentInitiated> events() {
            return List.copyOf(events);
        }

        void clear() {
            events.clear();
        }
    }

    @Autowired
    private InitiationService initiationService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private Customers customers;

    @Autowired
    private Accounts accounts;

    @Autowired
    private ProductEntitlements  productEntitlements;

    @Autowired
    private PaymentInitiatedTestListener paymentEvents;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        paymentEvents.clear();
    }

    @Test
    void createsSubmissionForValidCreditTransfer() {

        var request = new InitiateCreditTransfer(
                CUSTOMER_ID,
                DEBTOR_IBAN,
                CREDITOR_IBAN,
                "INV-12345",
                new BigDecimal("250.00"),
                Currency.getInstance("EUR"),
                LocalDate.of(2026, 11, 10));

        var outcome = initiationService.initiate(request);

        assertThat(outcome)
                .isInstanceOf(
                        InitiationOutcome.Accepted.class);

        var accepted =
                (InitiationOutcome.Accepted) outcome;

        assertThat(accepted.submissionId().value())
                .isPositive();

        var submission =
                submissionRepository.findById(
                        accepted.submissionId().value());


        assertThat(submission).isPresent();

        assertThat(submission.orElseThrow().getStatus())
                .isEqualTo(SubmissionStatus.ACCEPTED);

        assertThat(accepted.submissionId().value())
                .isPositive();

        assertThat(accepted.paymentId().value())
                .isPositive();

        assertThat(submission.get().getCustomerId())
                .isEqualTo(1L);

        assertThat(submission.get().getProductCode())
                .isEqualTo(ProductCode.SEPA_BULK_CT);

        assertThat(submission.get().getStatus())
                .isEqualTo(SubmissionStatus.ACCEPTED);

        assertThat(paymentEvents.events())
                .hasSize(1);

        assertThat(paymentEvents.events().getFirst().submissionId())
                .isEqualTo(accepted.submissionId());

        var status = jdbcTemplate.queryForObject(
                """
                select status
                from engine_payment
                where external_reference = ?
                """,
                String.class,
                accepted.paymentId().reference());

        assertThat(status)
                .isEqualTo("RECEIVED");
    }

    @Test
    void rejectsInactiveCustomer() {

        var outcome = initiationService.initiate(request(SUSPENDED_CUSTOMER_ID, DEBTOR_IBAN));

        assertThat(outcome)
                .isInstanceOf(InitiationOutcome.Rejected.class);

        var rejected = (InitiationOutcome.Rejected) outcome;

        assertThat(rejected.reasons())
                .extracting(InitiationRejection::code)
                .containsExactly(
                        InitiationRejectionCode.CUSTOMER_NOT_ACTIVE);

        assertThat(paymentEvents.events())
                .isEmpty();
    }

    @Test
    void rejectsInactiveAccount() {

        var outcome = initiationService.initiate(request(BLOCKED_ACCOUNT_CUSTOMER_ID, BLOCKED_ACCOUNT_IBAN));

        assertThat(outcome)
                .isInstanceOf(InitiationOutcome.Rejected.class);

        var rejected = (InitiationOutcome.Rejected) outcome;

        assertThat(rejected.reasons())
                .extracting(InitiationRejection::code)
                .containsExactly(
                        InitiationRejectionCode.ACCOUNT_NOT_ACTIVE);

        assertThat(paymentEvents.events())
                .isEmpty();
    }

    @Test
    void rejectsInactiveProduct() {

        var outcome = initiationService.initiate(request(BLOCKED_PRODUCT_CUSTOMER_ID, BLOCKED_PRODUCT_IBAN));

        assertThat(outcome)
                .isInstanceOf(InitiationOutcome.Rejected.class);

        var rejected = (InitiationOutcome.Rejected) outcome;

        assertThat(rejected.reasons())
                .extracting(InitiationRejection::code)
                .containsExactly(
                        InitiationRejectionCode.PRODUCT_NOT_ACTIVE);

        assertThat(paymentEvents.events())
                .isEmpty();
    }

    private InitiateCreditTransfer request(CustomerId customerId, Iban debtorIban) {

        return new InitiateCreditTransfer(
                customerId,
                debtorIban,
                CREDITOR_IBAN,
                "INV-12345",
                new BigDecimal("250.00"),
                Currency.getInstance("EUR"),
                LocalDate.of(2026, 11, 10));
    }

    private InitiationOutcome.Rejected reject(
            SubmissionId submissionId,
            InitiationRejectionCode code,
            String message) {

        submissionService.reject(submissionId);

        return new InitiationOutcome.Rejected(
                new InitiationRejection(code, message));
    }
}