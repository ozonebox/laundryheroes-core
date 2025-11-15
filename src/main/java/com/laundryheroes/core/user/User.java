package com.laundryheroes.core.user;

import jakarta.persistence.*;
import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 80)
    private String firstName;

    @Column(length = 80)
    private String lastName;

    @Column(length = 20)
    private String gender;

    @Column(length = 64)
    private String verificationAuthKey;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ProfileStatus profileStatus = ProfileStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private int failedAttempts = 0;

    @Column
    private Instant lastFailedAt;

    @Column(length = 10)
    private String verificationOtp;

    @Column
    private Instant otpExpiresAt;

    @Column
    private Instant lastOtpSentAt;

    @Column(length = 10)
    private String resetOtp;

    @Column
    private Instant resetOtpExpiresAt;

    @Column(length = 64)
    private String resetAuthKey;

    @Column
    private Instant lastResetOtpSentAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        if (this.profileStatus == null) {
            this.profileStatus = ProfileStatus.PENDING;
        }
        if (this.role == null) {
            this.role = UserRole.CUSTOMER;
        }
    }
}
