package com.laundryheroes.core.auth;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    /**
     * Create a fresh refresh token for user
     * Called during login + during rotation
     */
    @Transactional
    public RefreshToken create(User user) {
        String token = generateSecureToken();
        Instant now = Instant.now();

        RefreshToken rt = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(now.plusMillis(refreshExpirationMs))
                .revoked(false)
                .lastUsedAt(now)
                .build();

        return refreshTokenRepository.save(rt);
    }

    
    @Transactional
    public Optional<RefreshToken> validateAndRotate(String email,String token) {
        Instant now = Instant.now();
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        User user = optional.get();
        Optional<RefreshToken> opt = refreshTokenRepository.findByUserAndToken(user,token);

        if (opt.isEmpty()) return Optional.empty();

        RefreshToken rt = opt.get();

        // If token is revoked or expired → invalidate everything (replay attack)
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(now)) {
            revokeAllForUser(rt.getUser());
            return Optional.empty();
        }

        // Token is valid → rotate (issue new one)
        refreshTokenRepository.delete(rt);

        RefreshToken newToken = create(rt.getUser());

        return Optional.of(newToken);
    }
    @Transactional
    public void revokeAllForToken(String email,String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            return;
        }
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            return;
        }
        User user = optional.get();
        refreshTokenRepository.findByUserAndToken(user,tokenValue)
                .ifPresent(rt -> revokeAllForUser(rt.getUser()));
    }
    
    @Transactional
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    
    @Transactional
    public void revokeAllForUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    private String generateSecureToken() {
        return UUID.randomUUID().toString() + "." + UUID.randomUUID();
    }
}
