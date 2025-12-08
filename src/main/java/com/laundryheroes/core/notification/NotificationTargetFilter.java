package com.laundryheroes.core.notification;

import com.laundryheroes.core.user.UserRole;

import lombok.*;

@Data
@NoArgsConstructor
public class NotificationTargetFilter {
    private UserRole role;        // CUSTOMER / DRIVER / ADMIN
    private String city;        // later: Lagos, etc.
    private boolean onlyActive; // etc.
}

