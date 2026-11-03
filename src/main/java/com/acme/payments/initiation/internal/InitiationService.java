package com.acme.payments.initiation.internal;

import com.acme.payments.initiation.*;
import com.acme.payments.initiation.internal.payment.InitiatedPaymentService;
import com.acme.payments.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.acme.payments.initiation.InitiateCreditTransfer;
import com.acme.payments.initiation.InitiationOutcome;
import com.acme.payments.initiation.InitiationRejection;
import com.acme.payments.initiation.InitiationRejectionCode;
import com.acme.payments.initiation.PaymentId;
import com.acme.payments.initiation.PaymentInitiated;
import com.acme.payments.initiation.SubmissionId;
import com.acme.payments.initiation.internal.submission.SubmissionService;
import com.acme.payments.reference.AccountStatus;
import com.acme.payments.reference.Accounts;
import com.acme.payments.reference.CustomerStatus;
import com.acme.payments.reference.Customers;
import com.acme.payments.reference.EntitlementStatus;
import com.acme.payments.reference.ProductCode;
import com.acme.payments.reference.ProductEntitlements;

import static com.acme.payments.initiation.InitiationRejectionCode.ACCOUNT_NOT_ACTIVE;
import static com.acme.payments.initiation.InitiationRejectionCode.CUSTOMER_NOT_ACTIVE;
import static com.acme.payments.initiation.InitiationRejectionCode.PRODUCT_NOT_ACTIVE;

@Service
@RequiredArgsConstructor
public class InitiationService {

    private static final ProductCode PRODUCT =
            ProductCode.SEPA_BULK_CT;

    private final Customers customers;
    private final Accounts accounts;
    private final ProductEntitlements productEntitlements;
    private final SubmissionService submissionService;
    private final InitiatedPaymentService initiatedPaymentService;

    private final ApplicationEventPublisher events;

    @Transactional
    public InitiationOutcome initiate(
            InitiateCreditTransfer request) {

        SubmissionId submissionId =
                submissionService.create(
                        request.customerId(),
                        PRODUCT);

        var customer =
                customers.getCustomer(
                        request.customerId());

        if (customer.status() != CustomerStatus.ACTIVE) {
            return reject(
                    CUSTOMER_NOT_ACTIVE,
                    "Customer %s is not active"
                            .formatted(customer.customerId()));
        }

        var account =
                accounts.getAccount(
                        request.customerId(),
                        request.debtorIban());

        if (account.status() != AccountStatus.ACTIVE) {
            return reject(
                    ACCOUNT_NOT_ACTIVE,
                    "Account %s is not active"
                            .formatted(account.accountId()));
        }

        var entitlement =
                productEntitlements.getProductEntitlement(
                        request.customerId(),
                        PRODUCT);

        if (entitlement.status() != EntitlementStatus.ACTIVE) {
            return reject(
                    PRODUCT_NOT_ACTIVE,
                    "Product %s is not active for customer %s"
                            .formatted(
                                    PRODUCT,
                                    request.customerId()));
        }

        PaymentId paymentId =
                initiatedPaymentService.create(
                        submissionId,
                        account.accountId(),
                        request);

        submissionService.accept(submissionId);


        events.publishEvent(
                new PaymentInitiated(
                        submissionId,
                        paymentId,
                        account.accountId(),
                        request.debtorIban(),
                        request.creditorIban(),
                        request.endToEndId(),
                        request.amount(),
                        request.currency(),
                        request.requestedExecutionDate()));

        return new InitiationOutcome.Accepted(
                submissionId,
                paymentId);
    }

    private InitiationOutcome.Rejected reject(
            InitiationRejectionCode code,
            String message) {

        return new InitiationOutcome.Rejected(
                new InitiationRejection(
                        code,
                        message));
    }
}