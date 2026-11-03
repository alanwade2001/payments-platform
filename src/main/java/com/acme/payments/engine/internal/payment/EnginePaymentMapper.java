package com.acme.payments.engine.internal.payment;

import com.acme.payments.engine.CreditTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface EnginePaymentMapper {

    @Mapping(
            target = "id",
            ignore = true)
    @Mapping(
            target = "debtorIban",
            source = "debtorIban.value")
    @Mapping(
            target = "creditorIban",
            source = "creditorIban.value")
    @Mapping(
            target = "currency",
            expression =
                    "java(source.currency().getCurrencyCode())")
    @Mapping(
            target = "status",
            constant = "RECEIVED")
    EnginePaymentEntity toEntity(CreditTransfer source);
}