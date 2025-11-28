package com.laundryheroes.core.notification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FcmClient {

    public void sendToToken(
            String token,
            String title,
            String body,
            Map<String, Object> data
    ) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                    Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build()
                )
                .putAllData(convertData(data))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM sent successfully: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM send failed: {}", e.getMessage(), e);
            throw new RuntimeException("FCM push failed", e);
        }
    }

    private Map<String, String> convertData(Map<String, Object> data) {
        if (data == null) return Collections.emptyMap();

        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> e : data.entrySet()) {
            map.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return map;
    }
}
