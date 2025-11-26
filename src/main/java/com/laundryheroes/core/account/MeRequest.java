package com.laundryheroes.core.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MeRequest {

    @NotBlank
    @Email
    private String email;

}
