package com.eventhive.notification.service;

import com.eventhive.shared.events.OrderCreatedEvent;
import com.eventhive.shared.events.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationApplicationService.class);

    public void sendOrderConfirmation(OrderCreatedEvent event) {
        log.info("Notification sent: ORDER_CONFIRMATION to customer={} orderId={} amount={}",
            event.customerId(), event.orderId(), event.amount());
        // In production: email/SMS/push. For demo we only log.
    }

    public void sendPaymentConfirmation(PaymentCompletedEvent event) {
        log.info("Notification sent: PAYMENT_CONFIRMATION orderId={} transactionId={}",
            event.orderId(), event.transactionId());
    }
}
