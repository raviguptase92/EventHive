package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record NotificationEvent(
    @JsonProperty("notificationId") UUID notificationId,
    @JsonProperty("type") String type,
    @JsonProperty("recipient") String recipient,
    @JsonProperty("subject") String subject,
    @JsonProperty("body") String body,
    @JsonProperty("metadata") Map<String, String> metadata,
    @JsonProperty("timestamp") Instant timestamp
) {
    @JsonCreator
    public NotificationEvent {}
}
