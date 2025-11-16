package com.laundryheroes.core.auth;

import java.time.Instant;

import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "ix_refresh_token_token", columnList = "token", unique = true),
        @Index(name = "ix_refresh_token_user", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_refresh_token_user"))
    private User user;

    @Column(nullable = false, length = 255, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant lastUsedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        if (lastUsedAt == null) {
            this.lastUsedAt = now;
        }
    }
}
