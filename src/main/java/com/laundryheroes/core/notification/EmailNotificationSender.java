package com.laundryheroes.core.notification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.laundryheroes.core.email.EmailService;
import com.laundryheroes.core.email.EmailTemplateModel;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationChannelSender {

    private final EmailService emailService; // e.g. SES/SendGrid/Mailgun

    @Override
    public void send(User user, String title, String body, Map<String, Object> meta) {
        if (user.getEmail() == null) return;
        //emailService.sendEmail(user.getEmail(), title, body);
        emailService.sendTemplatedEmail(title, body, new EmailTemplateModel(
    title,
    body,
    null,
    null,
    "If you do not recognize this activity, please login and change your password now."
  ));
    }
}

