package com.laundryheroes.core.notification;

import lombok.*;

@Data
@NoArgsConstructor
public class NotificationTargetFilter {
    private String role;        // CUSTOMER / DRIVER / ADMIN
    private String city;        // later: Lagos, etc.
    private boolean onlyActive; // etc.
}

