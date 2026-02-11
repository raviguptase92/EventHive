package com.eventhive.inventory.consumer;

import com.eventhive.inventory.service.InventoryReservationService;
import com.eventhive.shared.events.OrderCreatedEvent;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

/**
 * Shared subscription: messages are distributed across consumers in round-robin.
 * No ordering guarantee; maximum throughput. Use when order-per-key is not required.
 */
@Component
public class OrderCreatedConsumerShared {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumerShared.class);
    private static final String TOPIC = "order-created";
    private static final String SUB_SHARED = "inventory-shared-sub";

    private final InventoryReservationService reservationService;

    public OrderCreatedConsumerShared(InventoryReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PulsarListener(
        subscriptionName = SUB_SHARED,
        topics = TOPIC,
        subscriptionType = SubscriptionType.Shared,
        concurrency = "3"
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("[Shared] Reserved inventory for orderId={}", event.orderId());
        reservationService.reserveForOrder(event);
    }
}
