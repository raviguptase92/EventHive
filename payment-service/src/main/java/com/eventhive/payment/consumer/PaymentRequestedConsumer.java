package com.eventhive.payment.consumer;

import com.eventhive.payment.config.PulsarConsumerConfig;
import com.eventhive.payment.service.PaymentApplicationService;
import com.eventhive.shared.events.PaymentRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestedConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentRequestedConsumer.class);
    private static final String TOPIC = "payment-requested";

    private final PaymentApplicationService paymentService;

    public PaymentRequestedConsumer(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    @PulsarListener(
        subscriptionName = PulsarConsumerConfig.SUBSCRIPTION_NAME,
        topics = TOPIC,
        consumerCustomizer = "paymentConsumerCustomizer"
    )
    public void onPaymentRequested(PaymentRequestedEvent event) {
        log.info("Received PaymentRequestedEvent orderId={} paymentId={}", event.orderId(), event.paymentId());
        paymentService.processPaymentRequest(event);
    }
}
