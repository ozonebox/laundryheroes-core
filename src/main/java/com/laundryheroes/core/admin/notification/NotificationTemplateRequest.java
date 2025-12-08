package com.laundryheroes.core.admin.notification;

import com.laundryheroes.core.notification.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class NotificationTemplateRequest {

    @NotNull
    private NotificationCategory category;  // ORDER_UPDATE, PROMOTION, SYSTEM_ALERT, SUPPORT

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
