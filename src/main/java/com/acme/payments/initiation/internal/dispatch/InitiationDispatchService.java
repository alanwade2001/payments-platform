package com.acme.payments.initiation.internal.dispatch;

import com.acme.payments.initiation.InitiationImported;
import com.acme.payments.initiation.internal.persistence.InitiationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiationDispatchService {

    private final InitiationRepository initiationRepository;
    private final InstructedTransactionMapper mapper;
    private final InstructedTransactionPublisher publisher;

    @Transactional
    @EventListener(InitiationImported.class)
    public void dispatch(InitiationImported event) {

        log.info("Dispatching InitiationImported event: {}", event);

        var initiation = initiationRepository
                .findById(event.initiationId())
                .orElseThrow();

        initiation.getPayments()
                .forEach(payment ->
                        payment.getTransactions()
                                .forEach(transaction -> {
                                    var instructedTransaction =
                                            mapper.map(payment, transaction);

                                    publisher.publish(instructedTransaction);
                                }));
    }
}
