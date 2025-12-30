package com.laundryheroes.core.account;

import com.laundryheroes.core.user.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminEditProfileRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email provided")
    private String email;

    private String firstName;
    private String lastName;
    private String gender;
     private UserRole role;
    // private String dob;         // or LocalDate depending on your model
    // private String country;
    // private String address;
    // private String avatarUrl;
}
