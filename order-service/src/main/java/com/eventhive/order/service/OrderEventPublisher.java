package com.eventhive.order.service;

import com.eventhive.shared.events.OrderCreatedEvent;
import com.eventhive.shared.events.PaymentRequestedEvent;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final String TOPIC_ORDER_CREATED = "order-created";
    private static final String TOPIC_PAYMENT_REQUESTED = "payment-requested";

    private final PulsarTemplate<OrderCreatedEvent> orderCreatedTemplate;
    private final PulsarTemplate<PaymentRequestedEvent> paymentRequestedTemplate;

    public OrderEventPublisher(
            PulsarTemplate<OrderCreatedEvent> orderCreatedTemplate,
            PulsarTemplate<PaymentRequestedEvent> paymentRequestedTemplate) {
        this.orderCreatedTemplate = orderCreatedTemplate;
        this.paymentRequestedTemplate = paymentRequestedTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        orderCreatedTemplate.newMessage(event)
            .withTopic(TOPIC_ORDER_CREATED)
            .withMessageCustomizer(mb -> mb.key(event.orderId().toString()))
            .send();
    }

    public void publishPaymentRequested(PaymentRequestedEvent event) {
        paymentRequestedTemplate.newMessage(event)
            .withTopic(TOPIC_PAYMENT_REQUESTED)
            .withMessageCustomizer(mb -> mb.key(event.orderId().toString()))
            .send();
    }
}
