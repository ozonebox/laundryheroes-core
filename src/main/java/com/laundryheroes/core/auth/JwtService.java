package com.laundryheroes.core.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.laundryheroes.core.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String generateFullToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .claim("authLevel", "FULL")
                .claim("profileStatus", user.getProfileStatus().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String generatePendingToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .claim("authLevel", "PENDING")
                .claim("profileStatus", user.getProfileStatus().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String extractAuthLevel(String token) {
    return getClaims(token).get("authLevel", String.class);
    }


    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email,String profileStatus) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();
        String claimsProfileStatus = claims.get("profileStatus").toString();
        String role = (String) claims.get("role");
        return subject != null && subject.equals(email) && expiration.after(new Date()) &&profileStatus.equalsIgnoreCase(claimsProfileStatus);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
