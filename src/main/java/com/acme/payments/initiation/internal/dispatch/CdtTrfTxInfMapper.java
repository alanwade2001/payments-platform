package com.acme.payments.initiation.internal.dispatch;


import iso.std.iso._20022.tech.xsd.pacs_008_001.*;
import iso.std.iso._20022.tech.xsd.pain_001_001.CreditTransferTransaction61;
import iso.std.iso._20022.tech.xsd.pain_001_001.PaymentInstruction44;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.util.Currency;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CdtTrfTxInfMapper {
    //@Mapping(target = "ct11.pmtId.txId", source = "txId")
    @Mapping(target = "instdAmt", source = "amt.instdAmt")
    @Mapping(target = "rgltryRptgs", ignore = true) // TODO - remove
    CreditTransferTransaction73 map(@MappingTarget CreditTransferTransaction73 ct11, CreditTransferTransaction61 ct10);

    @Mapping(target = "rgltryRptgs", ignore = true) // TODO - remove
    CreditTransferTransaction73 map(@MappingTarget CreditTransferTransaction73 ct11, PaymentInstruction44 pmtInf);

    default CreditTransferTransaction73 map( PaymentInstruction44 pmtInf
            , CreditTransferTransaction61 ct10
            , String txId
            , LocalDate settlementDate
    ) {
        var ct11 = new CreditTransferTransaction73();
        map(ct11, pmtInf);
        map(ct11, ct10);

        if(ct11.getPmtId() == null ) {
            ct11.setPmtId(new PaymentIdentification13());
        }

        ct11.getPmtId().setTxId(txId);

        if( ct11.getInstdAmt() == null ) {
            ct11.setInstdAmt(new ActiveOrHistoricCurrencyAndAmount());
        }

        ct11.getInstdAmt().setValue(ct10.getAmt().getInstdAmt().getValue());
        ct11.getInstdAmt().setCcy(ct10.getAmt().getInstdAmt().getCcy());

        if( ct11.getIntrBkSttlmAmt() == null) {
            ct11.setIntrBkSttlmAmt(new ActiveCurrencyAndAmount());
        }

        ct11.getIntrBkSttlmAmt().setValue(ct10.getAmt().getInstdAmt().getValue());
        ct11.getIntrBkSttlmAmt().setCcy(ct10.getAmt().getInstdAmt().getCcy());

        ct11.setIntrBkSttlmDt(settlementDate);

        ct11.setChrgBr(ChargeBearerType1Code.CRED);

        return ct11;
    }
}