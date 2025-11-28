package com.laundryheroes.core.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminRealtimeService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String ADMIN_TOPIC = "/topic/admin";

    public void push(
            String responseCode,
            String message,
            Map<String, Object> data
    ) {
        RealtimePayload payload = new RealtimePayload(
                responseCode,
                message,
                data
        );

        messagingTemplate.convertAndSend(ADMIN_TOPIC, payload);
    }
}
