package com.acme.payments.initiation.internal.dispatch;

import com.acme.payments.engine.*;
import com.acme.payments.initiation.model.Payment;
import com.acme.payments.initiation.model.Transaction;
import iso.std.iso._20022.tech.xsd.pain_001_001.CreditTransferTransaction61;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class InstructedTransactionMapper {

    private final CdtTrfTxInfMapper cdtTrfTxInfMapper;

    public InstructedTransaction map(
            Payment payment,
            Transaction transaction) {

        String txId = UUID.randomUUID().toString();
        // build pacs.008 CdtTrfTxInf
        var metadata = new InstructionMetadata(
          new TransactionId(txId),
                new SourceReference("Initiation", txId),
                new SchemeId("SCT")

        );

        var pacs008Transaction = cdtTrfTxInfMapper.map(payment.getPmtInf(),
                transaction.getCdtTrfTxInf(),
                txId,
                LocalDate.now());

        return new InstructedTransaction(
                metadata,
                pacs008Transaction
        );
    }
}
