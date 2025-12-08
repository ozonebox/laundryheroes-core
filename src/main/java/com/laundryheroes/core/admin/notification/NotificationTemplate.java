package com.laundryheroes.core.admin.notification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

import com.laundryheroes.core.notification.NotificationCategory;
import com.laundryheroes.core.notification.NotificationChannel;

@Entity
@Table(name = "notification_templates")
@Data
@NoArgsConstructor
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Title shown in UI
    @Column(nullable = false)
    private String title;

    // Template body with variables e.g. "Hi {{name}}, your order {{orderId}} is ready."
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationCategory category; // ORDER, PROMO, USER, SYSTEM, CUSTOM

    private boolean active = true;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
