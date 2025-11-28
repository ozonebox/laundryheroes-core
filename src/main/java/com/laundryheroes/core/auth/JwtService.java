package com.laundryheroes.core.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-ms}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs
    ) {
        this.secret = secret;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }


    public String generateAccessTokenFull(User user) {
        return buildAccessToken(user, "FULL");
    }

    public String generateAccessTokenPending(User user) {
        return buildAccessToken(user, "PENDING");
    }

    private String buildAccessToken(User user, String authLevel) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .claim("authLevel", authLevel)
                .claim("profileStatus", user.getProfileStatus().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }


    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpirationMs);

        // Secure random string, avoids JWT overhead
        String refreshTokenValue = UUID.randomUUID().toString() + "." + UUID.randomUUID();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("id", user.getId())
                .claim("type", "REFRESH")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String extractAuthLevel(String token) {
        return getClaims(token).get("authLevel", String.class);
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }


    public boolean isAccessTokenValid(String token, String email, String profileStatus) {
        Claims claims = getClaims(token);

        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();
        String claimsProfileStatus = claims.get("profileStatus", String.class);

        return subject != null &&
                subject.equals(email) &&
                expiration.after(new Date()) &&
                profileStatus.equalsIgnoreCase(claimsProfileStatus);
    }

    public boolean isRefreshToken(String token) {
        Claims claims = getClaims(token);
        return "REFRESH".equals(claims.get("type"));
    }
    public boolean isAccessTokenExpired(String token) {
    return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
