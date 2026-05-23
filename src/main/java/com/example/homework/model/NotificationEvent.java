package com.example.homework.model;

import java.time.OffsetDateTime;

public class NotificationEvent {
    private final Long id;
    private final Long userId;
    private final String type;
    private final String payloadJson;
    private final OffsetDateTime createdAt;

    public NotificationEvent(Long id, Long userId, String type, String payloadJson, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.payloadJson = payloadJson;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
