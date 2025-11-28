package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultNotificationService implements NotificationService {

    private final NotificationPreferenceRepository preferenceRepo;
    private final NotificationLogRepository logRepo;
    private final UserRepository userRepository;

    private final PushNotificationSender pushSender;
    private final EmailNotificationSender emailSender;
    private final WhatsappNotificationSender whatsappSender;

    @Override
    @Async
    public void sendToUser(
        User user,
        NotificationCategory category,
        String title,
        String body,
        Map<String, Object> meta
    ) {
        NotificationPreference pref = preferenceRepo
            .findByUserAndCategory(user, category)
            .orElseGet(() -> defaultPref(user, category));

        if (pref.isPushEnabled()) {
            sendAndLog(user, NotificationChannel.PUSH, category, title, body, meta);
        }
        if (pref.isEmailEnabled()) {
            sendAndLog(user, NotificationChannel.EMAIL, category, title, body, meta);
        }
        //allow only order related notifications via whatsapp
        if (pref.isWhatsappEnabled()) {
            sendAndLog(user, NotificationChannel.WHATSAPP, category, title, body, meta);
        }
    }

    @Override
    @Async
    public void broadcastToSegment(
        NotificationCategory category,
        String title,
        String body,
        NotificationTargetFilter filter,
        Map<String, Object> meta
    ) {
        // very simple example: filter by role (you can extend)
        List<User> users;
        if (filter.getRole() != null) {
            users = userRepository.findByRole(filter.getRole());
        } else {
            users = userRepository.findAll();
        }

        for (User user : users) {
            sendToUser(user, category, title, body, meta);
        }
    }

    private void sendAndLog(
        User user,
        NotificationChannel channel,
        NotificationCategory category,
        String title,
        String body,
        Map<String, Object> meta
    ) {
        boolean success = false;
        String error = null;

        try {
            getSender(channel).send(user, title, body, meta);
            success = true;
        } catch (Exception e) {
            error = e.getMessage();
        }

        NotificationLog log = new NotificationLog();
        log.setUser(user);
        log.setChannel(channel);
        log.setCategory(category);
        log.setTitle(title);
        log.setBody(body);
        log.setSuccess(success);
        log.setErrorMessage(error);

        logRepo.save(log);
    }

    private NotificationChannelSender getSender(NotificationChannel channel) {
        return switch (channel) {
            case PUSH -> pushSender;
            case EMAIL -> emailSender;
            case WHATSAPP -> whatsappSender;
        };
    }

    private NotificationPreference defaultPref(User user, NotificationCategory category) {
        NotificationPreference p = new NotificationPreference();
        p.setUser(user);
        p.setCategory(category);
        p.setPushEnabled(true);
        p.setEmailEnabled(true);
        p.setWhatsappEnabled(false);
        return preferenceRepo.save(p);
    }
}
