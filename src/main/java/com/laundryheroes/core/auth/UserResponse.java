package com.laundryheroes.core.auth;

import com.laundryheroes.core.user.ProfileStatus;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

     public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.profileStatus= user.getProfileStatus();
        this.role = user.getRole();
    }

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private ProfileStatus profileStatus;
    private String accessToken;
    private String refreshToken;
    private UserRole role;
    private double serviceFee;
    private double devliiveryFee;
}
