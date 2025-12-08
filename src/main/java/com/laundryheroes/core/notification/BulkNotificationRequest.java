package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BulkNotificationRequest {
    @NotEmpty
    private List<Long> userIds;

    private NotificationCategory category;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Map<String, Object> meta;
}
