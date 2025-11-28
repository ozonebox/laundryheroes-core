package com.laundryheroes.core.notification;

import java.util.Map;

import com.laundryheroes.core.user.User;

public interface NotificationChannelSender {
    void send(User user, String title, String body, Map<String, Object> meta);
}
