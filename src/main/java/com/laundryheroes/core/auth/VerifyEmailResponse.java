package com.laundryheroes.core.auth;

public class VerifyEmailResponse {

    private String authKey;

    private String email;

    public VerifyEmailResponse() {
    }

    public VerifyEmailResponse(String authKey,String email) {
        this.authKey = authKey;
        this.email = email;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getEmail() {
        return email;
    }
}
