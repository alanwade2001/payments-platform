package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.InstructedTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionId", source = "metadata.transactionId.value")
    @Mapping(target = "scheme", source = "metadata.schemeId.value")
    InstructedTransactionEntity toEntity(InstructedTransaction transaction);

    InstructedTransaction fromEntity(InstructedTransactionEntity entity);

    @Mapping(target = "instructedTransaction.id", source = "instructedTransactionId.value")
    ExecutionTransactionEntity toEntity(ExecutionTransaction transaction);

    @Mapping(target = "instructedTransactionId.value", source = "instructedTransaction.id")
    ExecutionTransaction fromEntity(ExecutionTransactionEntity entity);


    default ExecutionTransactionId mapFromLong( Long id) {
        if(id == null) return null;
        return new ExecutionTransactionId(id + "");
    }

    default Long map( ExecutionTransactionId id) {
        if(id == null) return null;
        return Long.parseLong(id.value());
    }


}
