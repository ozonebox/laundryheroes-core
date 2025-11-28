package com.laundryheroes.core.notification;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUserNotificationRequest {
    @NotNull
    private NotificationCategory category;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Map<String, Object> meta = new HashMap<>();
}
