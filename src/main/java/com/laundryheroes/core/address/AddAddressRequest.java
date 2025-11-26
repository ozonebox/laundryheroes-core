package com.laundryheroes.core.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddAddressRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotNull
    private Long lat;

    @NotNull
    private Long lng;

    @NotBlank
    private String label;

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public Long getLat() { return lat; }
    public Long getLng() { return lng; }
    public String getLabel() { return label; }
}
