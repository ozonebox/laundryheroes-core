package com.laundryheroes.core.address;

import jakarta.validation.constraints.NotBlank;

public class AddAddressRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}
