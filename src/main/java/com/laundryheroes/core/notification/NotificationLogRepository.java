package com.laundryheroes.core.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
     List<NotificationLog> findTop100ByChannelOrderByCreatedAtDesc(
            NotificationChannel channel
    );
    List<NotificationLog> findTop100ByUserAndChannelOrderByCreatedAtDesc(User user,
            NotificationChannel channel
    );
}
