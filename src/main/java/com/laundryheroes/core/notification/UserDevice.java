package com.laundryheroes.core.notification;

import java.time.Instant;

import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_devices")
@Data
@NoArgsConstructor
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 255)
    private String deviceToken;  // FCM token

    @Column(nullable = false, length = 20)
    private String platform; // IOS / ANDROID

    @Column(nullable = false)
    private boolean active = true;

    private Instant lastSeenAt;
}
