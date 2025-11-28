package com.laundryheroes.core.notification;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminBroadcastRequest {
    @NotNull
    private NotificationCategory category;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private String role; // optional filter: CUSTOMER/DRIVER/etc
    private String city; // later

    private Map<String, Object> meta = new HashMap<>();
}
