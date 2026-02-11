package com.eventhive.inventory.service;

import com.eventhive.shared.events.InventoryReservedEvent;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryReservedPublisher {

    private static final String TOPIC = "inventory-reserved";

    private final PulsarTemplate<InventoryReservedEvent> pulsarTemplate;

    public InventoryReservedPublisher(PulsarTemplate<InventoryReservedEvent> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void publish(InventoryReservedEvent event) {
        pulsarTemplate.newMessage(event)
            .withTopic(TOPIC)
            .withMessageCustomizer(mb -> mb.key(event.orderId().toString()))
            .send();
    }
}
