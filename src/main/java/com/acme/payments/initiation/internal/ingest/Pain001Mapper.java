package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.model.Initiation;
import com.acme.payments.initiation.InitiationStatus;
import com.acme.payments.initiation.model.Payment;
import com.acme.payments.initiation.model.Transaction;
import iso.std.iso._20022.tech.xsd.pain_001_001.CreditTransferTransaction61;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import iso.std.iso._20022.tech.xsd.pain_001_001.PaymentInstruction44;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class Pain001Mapper {

    public Initiation map(Document document) {

        var customerCreditTransfer =
                document.getCstmrCdtTrfInitn();

        var groupHeader =
                customerCreditTransfer.getGrpHdr();

        var initiation = Initiation.builder()
                .customerId(
                        Pain001CustomerIdResolver.resolve(document))
                .messageId(groupHeader.getMsgId())
                .sourceCreatedAt(
                        groupHeader.getCreDtTm().toInstant())
                .receivedAt(Instant.now())
                .status(InitiationStatus.ACCEPTED)
                .build();

        customerCreditTransfer.getPmtInves()
                .stream()
                .map(this::mapPayment)
                .forEach(initiation::addPayment);

        return initiation;
    }

    private Payment mapPayment(PaymentInstruction44 paymentInstruction) {

        var cache = new ArrayList<>(paymentInstruction.getCdtTrfTxInves());
        paymentInstruction.getCdtTrfTxInves().clear();

        var payment = Payment.builder()
                .paymentInformationId(
                        paymentInstruction.getPmtInfId())
                .debtorName(
                        paymentInstruction.getDbtr().getNm())
                .debtorIban(
                        paymentInstruction
                                .getDbtrAcct()
                                .getId()
                                .getIBAN())
                .requestedExecutionDate(
                        paymentInstruction
                                .getReqdExctnDt()
                                .getDt())
                .pmtInf(paymentInstruction)
                .build();

        cache
                .stream()
                .map(this::mapTransaction)
                .forEach(payment::addTransaction);

        return payment;
    }

    private Transaction mapTransaction(
            CreditTransferTransaction61 transaction) {

        var instructedAmount =
                transaction.getAmt().getInstdAmt();

        return Transaction.builder()
                .endToEndId(
                        transaction
                                .getPmtId()
                                .getEndToEndId())
                .amount(instructedAmount.getValue())
                .currency(instructedAmount.getCcy())
                .creditorName(
                        transaction.getCdtr().getNm())
                .creditorIban(
                        transaction
                                .getCdtrAcct()
                                .getId()
                                .getIBAN())
                .cdtTrfTxInf(transaction)
                .build();
    }
}