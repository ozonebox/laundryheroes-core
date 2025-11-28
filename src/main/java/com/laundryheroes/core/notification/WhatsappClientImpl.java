package com.laundryheroes.core.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WhatsappClientImpl implements WhatsappClient {

    @Override
    public void sendMessage(String phone, String message) {
        log.info("[WHATSAPP STUB] To={} Message={}", phone, message);
    }
}
