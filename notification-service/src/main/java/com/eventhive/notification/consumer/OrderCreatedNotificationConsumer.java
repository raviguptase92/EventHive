package com.eventhive.notification.consumer;

import com.eventhive.notification.service.NotificationApplicationService;
import com.eventhive.shared.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedNotificationConsumer.class);
    private static final String TOPIC = "order-created";
    private static final String SUB = "notification-order-created-sub";

    private final NotificationApplicationService notificationService;

    public OrderCreatedNotificationConsumer(NotificationApplicationService notificationService) {
        this.notificationService = notificationService;
    }

    @PulsarListener(subscriptionName = SUB, topics = TOPIC)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Sending order confirmation for orderId={}", event.orderId());
        notificationService.sendOrderConfirmation(event);
    }
}
