package com.eventhive.payment.config;

import com.eventhive.shared.events.PaymentRequestedEvent;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.ConsumerBuilderCustomizer;

import java.util.concurrent.TimeUnit;

/**
 * Configures Payment consumer with:
 * - Dead Letter Topic for failed messages after retries
 * - Negative ack redelivery delay for retry behavior
 */
@Configuration
public class PulsarConsumerConfig {

    public static final String SUBSCRIPTION_NAME = "payment-service-sub";
    public static final String DEAD_LETTER_TOPIC = "payment-requested-dlq";

    @Bean
    public ConsumerBuilderCustomizer<PaymentRequestedEvent> paymentConsumerCustomizer() {
        return (ConsumerBuilder<PaymentRequestedEvent> builder) -> {
            builder
                .deadLetterPolicy(DeadLetterPolicy.builder()
                    .maxRedeliverCount(5)
                    .deadLetterTopic(DEAD_LETTER_TOPIC)
                    .retryLetterTopic("payment-requested-retry")
                    .build())
                .negativeAckRedeliveryDelay(2, TimeUnit.SECONDS);
        };
    }
}
