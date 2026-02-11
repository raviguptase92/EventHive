package com.eventhive.notification.consumer;

import com.eventhive.notification.service.NotificationApplicationService;
import com.eventhive.shared.events.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentCompletedNotificationConsumer.class);
    private static final String TOPIC = "payment-completed";
    private static final String SUB = "notification-payment-completed-sub";

    private final NotificationApplicationService notificationService;

    public PaymentCompletedNotificationConsumer(NotificationApplicationService notificationService) {
        this.notificationService = notificationService;
    }

    @PulsarListener(subscriptionName = SUB, topics = TOPIC)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Sending payment confirmation for orderId={}", event.orderId());
        notificationService.sendPaymentConfirmation(event);
    }
}
