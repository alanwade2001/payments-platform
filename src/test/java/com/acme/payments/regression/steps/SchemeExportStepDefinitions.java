package com.acme.payments.regression.steps;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.SchemeId;
import com.acme.payments.engine.internal.ExecutionService;
import com.acme.payments.engine.internal.ExportInstruction;
import com.acme.payments.engine.internal.ExportService;
import com.acme.payments.regression.states.PaymentInitiationContext;
import com.acme.payments.regression.states.SchemeExportScenarioState;
import com.acme.payments.regression.support.CapturingSepaCTGateway;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import iso.std.iso._20022.tech.xsd.pacs_008_001.Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class SchemeExportStepDefinitions {

    private final ExecutionService executionService;
    private final ExportService exportService;
    private final CapturingSepaCTGateway gateway;
    private final PaymentInitiationContext context;
    private final SchemeExportScenarioState state;

    @Before("@scheme-export")
    public void resetSchemeExportState() {
        gateway.reset();
        state.reset();
    }

    @And("the accepted transactions have been prepared for execution")
    public void acceptedTransactionHasBeenPreparedForExecution() {

        ExecutionTransaction transaction =
                context.getExecutionTransaction();

        assertThat(transaction)
                .as("prepared execution transaction")
                .isNotNull();

        assertThat(transaction.id())
                .as("execution transaction ID")
                .isNotNull();

        assertThat(transaction.status())
                .as("execution transaction status")
                .isEqualTo(ExecutionStatus.READY);
    }

    @When("the ready transactions are exported to the {string} scheme")
    public void readyTransactionIsExportedToTheScheme(
            String schemeReference) {

        ExecutionTransaction transaction =
                context.getExecutionTransaction();

        assertThat(transaction)
                .as("prepared execution transaction")
                .isNotNull();

        var instruction = new ExportInstruction(
                new SchemeId(schemeReference),
                transaction.executionDate());

        exportService.export(instruction);
    }

    @Then("a pacs008 message is sent to the SEPA Credit Transfer gateway")
    public void a_pacs008_message_is_sent_to_the_sepa_credit_transfer_gateway() {
        assertThat(gateway.messageCount())
                .as("number of SEPA Credit Transfer gateway messages")
                .isEqualTo(1);
    }

    @Then("the pacs008 message contains all prepared transactions")
    public void pacs008MessageIsSentToTheGateway()
            throws JAXBException {

        Document pacs008 =
                unmarshal(gateway.lastMessage());

        assertThat(pacs008)
                .as("generated pacs.008 document")
                .isNotNull();

        assertThat(pacs008.getFIToFICstmrCdtTrf())
                .as("FIToFICustomerCreditTransfer")
                .isNotNull();

        state.setPacs008(pacs008);
    }

    @And("the pacs008 message contains the prepared transaction")
    public void pacs008ContainsPreparedTransaction() {

        Document pacs008 = state.getPacs008();

        assertThat(pacs008)
                .as("captured pacs.008 document")
                .isNotNull();

        var creditTransfer =
                pacs008.getFIToFICstmrCdtTrf();

        assertThat(creditTransfer.getCdtTrfTxInves())
                .as("pacs.008 credit transfer transactions")
                .hasSize(1);

        assertThat(creditTransfer.getGrpHdr().getNbOfTxs())
                .as("pacs.008 group header transaction count")
                .isEqualTo("1");
    }

    private Document unmarshal(byte[] message)
            throws JAXBException {

        JAXBContext jaxbContext =
                JAXBContext.newInstance(Document.class);

        Unmarshaller unmarshaller =
                jaxbContext.createUnmarshaller();

        try (var input =
                     new ByteArrayInputStream(message)) {

            return (Document) unmarshaller.unmarshal(input);

        } catch (java.io.IOException exception) {
            throw new IllegalStateException(
                    "Unable to close pacs.008 input stream",
                    exception);
        }
    }

    private Document unmarshal(String message)
            throws JAXBException {

        return unmarshal(
                message.getBytes(UTF_8));
    }
}