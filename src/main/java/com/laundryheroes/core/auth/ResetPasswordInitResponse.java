package com.laundryheroes.core.auth;

public class ResetPasswordInitResponse {

    private String resetAuthKey;
    private String email;

    public ResetPasswordInitResponse(String resetAuthKey,String email) {
        this.resetAuthKey = resetAuthKey;
        this.email = email;
    }

    public String getResetAuthKey() {
        return resetAuthKey;
    }
    public String getEmail() {
        return email;
    }
}
