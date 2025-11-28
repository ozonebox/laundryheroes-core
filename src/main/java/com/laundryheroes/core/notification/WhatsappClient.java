package com.laundryheroes.core.notification;

public interface WhatsappClient {
    void sendMessage(String phoneNumber, String message);
}
