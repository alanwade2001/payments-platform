package com.acme.payments.engine.schemes.sepa.ct;

import com.acme.payments.engine.ExecutionTransaction;
import iso.std.iso._20022.tech.xsd.pacs_008_001.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class SepaCTPacs008Mapper {

    private final Clock clock;
    private final SepaCTProperties configuration;

    Document map(List<ExecutionTransaction> transactions) {

        var message =
                new FIToFICustomerCreditTransferV14();

        message.setGrpHdr(
                createGroupHeader(transactions));

        message.getCdtTrfTxInves().addAll(
                createTransactions(transactions));

        var document = new Document();
        document.setFIToFICstmrCdtTrf(message);

        return document;
    }

    private List<CreditTransferTransaction73> createTransactions(List<ExecutionTransaction> transactions) {
        return transactions.stream()
                .map(ExecutionTransaction::transaction)
                .toList();
    }

    private GroupHeader131 createGroupHeader(
            List<ExecutionTransaction> transactions) {

        var header = new GroupHeader131();

        header.setMsgId("TODO");
        header.setCreDtTm(OffsetDateTime.now(clock));
        header.setNbOfTxs((long) transactions.size());

        // settlement information
        header.setSttlmInf(new SettlementInstruction15());
        header.getSttlmInf().setSttlmMtd(configuration.settlementMethod());
        // instructing agent
        header.setInstgAgt(setAgentBIC(configuration.instructingAgentBic()));
        // instructed agent
        header.setInstdAgt(setAgentBIC(configuration.instructedAgentBic()));

        return header;
    }

    private BranchAndFinancialInstitutionIdentification8 setAgentBIC( String bic ) {
        var b = new BranchAndFinancialInstitutionIdentification8();
        b.setFinInstnId(new FinancialInstitutionIdentification23());
        b.getFinInstnId().setBICFI(bic);

        return b;
    }
}
