package com.laundryheroes.core.notification;

import java.util.Map;

import com.laundryheroes.core.user.User;

public interface NotificationService {

    void sendToUser(
        User user,
        NotificationCategory category,
        String title,
        String body,
        Map<String, Object> meta
    );

    void broadcastToSegment(
        NotificationCategory category,
        String title,
        String body,
        NotificationTargetFilter filter,
        Map<String, Object> meta
    );
}
