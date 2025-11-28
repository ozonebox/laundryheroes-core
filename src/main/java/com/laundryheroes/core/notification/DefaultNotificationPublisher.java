package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultNotificationPublisher implements NotificationPublisher {

    private final NotificationService notificationService;
    private final NotificationTemplateResolver resolver;

    @Override
    public void notifyUser(
        User user,
        NotificationCategory category,
        NotificationTemplate template,
        Map<String, Object> context
    ) {
        NotificationMessage msg = resolver.resolve(template, context);
        notificationService.sendToUser(
            user,
            category,
            msg.getTitle(),
            msg.getBody(),
            context
        );
    }

    @Override
    public void notifyUsers(
        List<User> users,
        NotificationCategory category,
        NotificationTemplate template,
        Map<String, Object> context
    ) {
        NotificationMessage msg = resolver.resolve(template, context);
        for (User user : users) {
            notificationService.sendToUser(
                user,
                category,
                msg.getTitle(),
                msg.getBody(),
                context
            );
        }
    }
}
