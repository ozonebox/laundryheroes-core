package com.laundryheroes.core.auth;

import java.time.Instant;
import java.util.List;

import com.laundryheroes.core.address.AddressResponse;
import com.laundryheroes.core.user.ProfileStatus;
import com.laundryheroes.core.user.UserRole;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseAdmin {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private ProfileStatus profileStatus;
    private String accessToken;
    private String refreshToken;
    private UserRole role;
    private Instant createdAt;
    private int failedAttempts;
    private Instant lastFailedAt;
    private Instant otpExpiresAt;
    private Instant lastOtpSentAt;
    private Instant resetOtpExpiresAt;
    private Instant lastResetOtpSentAt;
    private List<AddressResponse> addresses;
}
