package com.laundryheroes.core.notification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WhatsappNotificationSender implements NotificationChannelSender {

    private final WhatsappClient whatsappClient;

    @Override
    public void send(User user, String title, String body, Map<String, Object> meta) {
        if (user.getPhoneNumber() == null) return;
        whatsappClient.sendMessage(user.getPhoneNumber(), body);
    }
}
