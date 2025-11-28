package com.laundryheroes.core.notification;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationLogResponse {

    private Long id;
    private Long userId;
    private String userEmail;

    private NotificationCategory category;
    private NotificationChannel channel;

    private String title;
    private String body;
    private boolean read;
    private boolean success;
    private String errorMessage;

    private Instant createdAt;
}
