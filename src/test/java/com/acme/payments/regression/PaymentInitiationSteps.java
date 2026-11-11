package com.acme.payments.regression;

import com.acme.payments.engine.internal.EngineAdminController;
import com.acme.payments.initiation.InitiationStatus;
import com.acme.payments.reference.AccountNotFoundException;
import com.acme.payments.reference.ProductCode;
import com.acme.payments.reference.ProductEntitlement;
import com.acme.payments.regression.clients.EngineAdmin;
import com.acme.payments.regression.clients.InitiationClient;
import com.acme.payments.regression.clients.ReferenceAdmin;
import com.acme.payments.regression.clients.TestApiClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class PaymentInitiationSteps {

    private final PaymentInitiationContext context;
    private final ReferenceAdmin referenceAdmin;
    private final InitiationClient  initiationClient;
    private final EngineAdmin engineAdmin;




    @Before
    public void reset() {
        context.reset();
    }

    @Given("customer {string} exists")
    public void customer_exists(String customerName) {

        var result = referenceAdmin.getCustomer(customerName);

        if( result.isEmpty() ) {
            log.info("Customer with name {} does not exist", customerName);
            var customerId = referenceAdmin.createCustomer(customerName);
            result = referenceAdmin.getCustomer(customerName);
        }

        var customer = result.orElseThrow();
        log.info("Customer with name {} exists", customerName);

        context.setCustomer( customer );
    }

    @Given("customer {string} has an account with IBAN {string}")
    public void customer_has_an_account_with_iban(String customerName, String debtorIban) {
        var customer = context.getCustomer();
        var result = referenceAdmin.getCustomerAccount(customer, debtorIban);

        if( result.isEmpty() ) {
            var accountId = referenceAdmin.createCustomerAccount(customer.customerId(), debtorIban, Currency.getInstance("EUR"));
            result = referenceAdmin.getCustomerAccount(customer, debtorIban);
        }

        var account = result.orElseThrow();
        log.info("Customer Account with name {} and iban {} exists", customerName, debtorIban);

        context.setAccount(account);
    }

    @Given("customer {string} is entitled to {string}")
    public void customer_is_entitled_to(String customerName, String productCode) {
        var customer = context.getCustomer();
        var pc = ProductCode.valueOf(productCode);
        var result = referenceAdmin.getCustomerEntitlement(customer.customerId(), pc);
        ProductEntitlement entitlement;
        if (result.isEmpty()) {
            var id = referenceAdmin.createCustomerEntitlement(customer.customerId(), productCode);
            entitlement = referenceAdmin.getCustomerEntitlement(customer.customerId(), pc).orElseThrow();
        } else {
            entitlement = result.orElseThrow();
        }

        log.info("Customer entitlement with name {} and entitlement {} exists", customerName, entitlement);
    }

    @Given("an SCT pain.{int} payment initiation")
    public void an_sct_pain_payment_initiation(Integer int1) {
        var testData = Pain001TestData.validSct();
        context.setPain001(testData);
    }

    @Given("the debtor IBAN is {string}")
    public void the_debtor_iban_is(String iban) {
        context.getPain001().setDebtorIban(iban);
    }

    @When("the payment initiation is submitted")
    public void the_payment_initiation_is_submitted() {
        context.getPain001().setCustomerDetails(context.getCustomer());
        String uniqueEndToEndId = UUID.randomUUID().toString().replace("-", "");
        context.getPain001().setEndToEnd(uniqueEndToEndId);
        var xml = context.getPain001().xml();

        try {
            var id = initiationClient.initiatePayment(xml);
            context.setInitiationId(id);
            log.info("Payment initiation with id {}", id);
        } catch( TestApiClient.TestApiException e ) {
            context.setSubmittedException( e.getCause() );
        }
    }

    @Then("the payment initiation is {string}")
    public void the_payment_initiation_is(String status) {

        var id = context.getInitiationId();
        var result = initiationClient.getInitiationDetails(id);

        assertThat(result.isPresent()).isTrue();
        var details = result.orElseThrow();

        context.setInitiationDetails(details);
        assertThat(details.status().name()).isEqualToIgnoringCase(status);
    }

    @Then("an instructed transaction is created")
    public void an_instructed_transaction_is_created() {
        var endToEndId = context.getPain001()
                .getEndToEndId();

        var result = engineAdmin.getInstructedTransaction(endToEndId);
        assertThat(result.isPresent()).isTrue();
        context.setInstructedTransaction(result.orElseThrow());
    }

    @Then("an execution transaction is created")
    public void an_execution_transaction_is_created() {
        var instructedTransactionId = context.getInstructedTransaction().id();
        var result = engineAdmin.getExecutionTransaction(instructedTransactionId);
        assertThat(result.isPresent()).isTrue();
        context.setExecutionTransaction(result.orElseThrow());
    }

    @Then("the execution transaction has status {string}")
    public void the_execution_transaction_has_status(String status) {
        var executionTransaction = context.getExecutionTransaction();
        assertThat(executionTransaction.status().name()).isEqualTo(status);
    }

    @Then("the rejection reason is {string}")
    public void the_rejection_reason_is(String reason) {
        var e = context.getSubmittedException();
        var root = e.getCause();
        var clazz = AccountNotFoundException.class;
        if( root.getClass().isAssignableFrom(clazz) ) {
            String message = root.getMessage();
            assertThat(message).contains(reason);
        } else{
            throw new RuntimeException("Unexpected exception!!", root);
        }
    }

    @Then("no instructed transaction is created")
    public void no_instructed_transaction_is_created() {
        var endToEndId = context.getPain001()
                .getEndToEndId();

        var result = engineAdmin.getInstructedTransaction(endToEndId);
        assertThat(result.isPresent()).isFalse();
    }

    @Then("no execution transaction is created")
    public void no_execution_transaction_is_created() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
