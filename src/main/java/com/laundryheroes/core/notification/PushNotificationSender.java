package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationSender implements NotificationChannelSender {

    private final UserDeviceRepository deviceRepo;
    private final FcmClient fcmClient;

    @Override
    public void send(User user, String title, String body, Map<String, Object> meta) {
        List<UserDevice> devices = deviceRepo.findByUserAndActiveTrue(user);
        if (devices.isEmpty()) {
            log.info("No active devices for user {}", user.getId());
            return;
        }

        for (UserDevice device : devices) {
            try {
                fcmClient.sendToToken(
                    device.getDeviceToken(),
                    title,
                    body,
                    meta
                );
            } catch (Exception e) {
                log.warn("Push failed for device {} (user {})",
                        device.getId(), user.getId());
            }
        }
    }
}
