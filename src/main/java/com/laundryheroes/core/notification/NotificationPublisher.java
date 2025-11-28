package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;

import com.laundryheroes.core.user.User;

public interface NotificationPublisher {

    void notifyUser(
        User user,
        NotificationCategory category,
        NotificationTemplate template,
        Map<String, Object> context
    );

    void notifyUsers(
        List<User> users,
        NotificationCategory category,
        NotificationTemplate template,
        Map<String, Object> context
    );
}
