package com.eventhive.inventory.consumer;

import com.eventhive.inventory.service.InventoryReservationService;
import com.eventhive.shared.events.OrderCreatedEvent;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

/**
 * Key_Shared subscription: messages with the same key (orderId) are delivered to the same consumer.
 * Guarantees ordering per key while allowing horizontal scaling.
 */
@Component
public class OrderCreatedConsumerKeyShared {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumerKeyShared.class);
    private static final String TOPIC = "order-created";
    private static final String SUB_KEY_SHARED = "inventory-key-shared-sub";

    private final InventoryReservationService reservationService;

    public OrderCreatedConsumerKeyShared(InventoryReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PulsarListener(
        subscriptionName = SUB_KEY_SHARED,
        topics = TOPIC,
        subscriptionType = SubscriptionType.Key_Shared,
        concurrency = "3"
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("[Key_Shared] Reserved inventory for orderId={}", event.orderId());
        reservationService.reserveForOrder(event);
    }
}
