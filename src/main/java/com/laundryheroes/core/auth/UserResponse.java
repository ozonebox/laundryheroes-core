package com.laundryheroes.core.auth;

import com.laundryheroes.core.user.ProfileStatus;

public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private ProfileStatus profileStatus;
    private String token;

    public UserResponse() {
    }

    public UserResponse(Long id, String email, String firstName, String lastName, String gender, ProfileStatus profileStatus) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.profileStatus = profileStatus;
    }

    public Long getId() {
        return id;
    }
     public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token=token;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }
}
