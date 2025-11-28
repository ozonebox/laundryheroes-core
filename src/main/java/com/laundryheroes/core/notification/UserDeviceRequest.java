package com.laundryheroes.core.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDeviceRequest {

    @NotBlank
    private String deviceToken;

    @NotBlank
    private String platform; // IOS / ANDROID
}
