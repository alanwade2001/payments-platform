package com.acme.payments.engine.internal.persistence;

import com.acme.payments.engine.ExecutionStatus;
import com.acme.payments.engine.ExecutionTransaction;
import com.acme.payments.engine.ExecutionTransactionId;
import com.acme.payments.engine.SchemeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExecutionTransactionRepository extends JpaRepository<ExecutionTransactionEntity, Long> {
    default Optional<ExecutionTransactionEntity> findById(ExecutionTransactionId id) {
        try {
            return findById(Long.parseLong(id.value()));
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
        update ExecutionTransactionEntity e
           set e.status = :newStatus
         where e.id = :id and
           e.status = :oldStatus
        """)
    int updateStatusById(
            @Param("id") Long id,
            @Param("newStatus") ExecutionStatus newStatus,
            @Param("oldStatus") ExecutionStatus oldStatus);



    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query("""
    update ExecutionTransactionEntity e
       set e.status = :newStatus
     where e.id in :ids and
           e.status = :oldStatus
    """)
    int updateStatusByEntityId(
            @Param("ids") List<Long> ids,
            @Param("newStatus") ExecutionStatus newStatus,
            @Param("oldStatus") ExecutionStatus oldStatus);


    default int updateStatusById(List<ExecutionTransactionId> ids, ExecutionStatus newState, ExecutionStatus oldState) {
        var lids = ids.stream()
                .map(ExecutionTransactionId::value)
                .map(Long::parseLong)
                .toList();

        return updateStatusByEntityId(lids, newState, oldState);
    }



    @Query("""
    select e
      from ExecutionTransactionEntity e
     where e.schemeId = :schemeId
       and e.status = :status
       and e.executionDate <= :executionDate
     order by e.id
    """)
    List<ExecutionTransactionEntity> findExecutionForStatus(
            @Param("schemeId") SchemeId schemeId,
            @Param("status") ExecutionStatus status,
            @Param("executionDate") LocalDate executionDate);
}
