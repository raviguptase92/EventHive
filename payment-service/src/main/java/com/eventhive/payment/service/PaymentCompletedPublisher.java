package com.eventhive.payment.service;

import com.eventhive.shared.events.PaymentCompletedEvent;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedPublisher {

    private static final String TOPIC = "payment-completed";

    private final PulsarTemplate<PaymentCompletedEvent> pulsarTemplate;

    public PaymentCompletedPublisher(PulsarTemplate<PaymentCompletedEvent> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void publish(PaymentCompletedEvent event) {
        pulsarTemplate.newMessage(event)
            .withTopic(TOPIC)
            .withMessageCustomizer(mb -> mb.key(event.orderId().toString()))
            .send();
    }
}
