package com.laundryheroes.core.auth;

import com.laundryheroes.core.user.ProfileStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private ProfileStatus profileStatus;
    private String accessToken;
    private String refreshToken;
}
