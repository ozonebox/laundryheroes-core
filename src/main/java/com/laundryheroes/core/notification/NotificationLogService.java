package com.laundryheroes.core.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository logRepository;

    public List<NotificationLogResponse> getMyLatestPushNotifications(User user) {

        return logRepository
                .findTop100ByUserAndChannelOrderByCreatedAtDesc(user,
                        NotificationChannel.PUSH
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationLogResponse> getLatestPushNotifications() {

        return logRepository
                .findTop100ByChannelOrderByCreatedAtDesc(
                        NotificationChannel.PUSH
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationLogResponse toResponse(NotificationLog log) {
        return NotificationLogResponse.builder()
                .id(log.getId())
                .userId(log.getUser() != null ? log.getUser().getId() : null)
                .userEmail(log.getUser() != null ? log.getUser().getEmail() : null)
                .category(log.getCategory())
                .channel(log.getChannel())
                .title(log.getTitle())
                .body(log.getBody())
                .success(log.isSuccess())
                .errorMessage(log.getErrorMessage())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
