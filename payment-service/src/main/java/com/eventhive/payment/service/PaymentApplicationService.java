package com.eventhive.payment.service;

import com.eventhive.payment.entity.PaymentEntity;
import com.eventhive.payment.repository.PaymentRepository;
import com.eventhive.shared.events.PaymentCompletedEvent;
import com.eventhive.shared.events.PaymentRequestedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;
    private final PaymentCompletedPublisher completedPublisher;

    public PaymentApplicationService(PaymentRepository paymentRepository,
                                    PaymentCompletedPublisher completedPublisher) {
        this.paymentRepository = paymentRepository;
        this.completedPublisher = completedPublisher;
    }

    @Transactional
    public void processPaymentRequest(PaymentRequestedEvent event) {
        // Idempotency: skip if already processed
        paymentRepository.findByIdempotencyKey(event.idempotencyKey())
            .ifPresent(ignored -> { /* already processed */ return; });

        var payment = new PaymentEntity();
        payment.setId(event.paymentId());
        payment.setOrderId(event.orderId());
        payment.setAmount(event.amount());
        payment.setCurrency(event.currency());
        payment.setStatus("COMPLETED");
        payment.setTransactionId("tx-" + UUID.randomUUID());
        payment.setIdempotencyKey(event.idempotencyKey());
        payment.setCreatedAt(Instant.now());

        paymentRepository.save(payment);

        PaymentCompletedEvent completed = new PaymentCompletedEvent(
            payment.getId(),
            payment.getOrderId(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getTransactionId(),
            Instant.now()
        );
        completedPublisher.publish(completed);
    }
}
