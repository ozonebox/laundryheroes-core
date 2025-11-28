package com.laundryheroes.core.notification;

import java.time.Instant;

import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications_log")
@Data
@NoArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    private String title;
    @Column(length = 2000)
    private String body;

    private boolean success;

    
    private String errorMessage;

    private Instant createdAt = Instant.now();
}

