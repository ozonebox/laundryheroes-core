package com.laundryheroes.core.address;

public class AddressResponse {

    private final Long id;
    private final String name;
    private final String phone;
    private final String address;
    private final boolean isDefault;

    public AddressResponse(Long id, String name, String phone, String address, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isDefault() { return isDefault; }
}
