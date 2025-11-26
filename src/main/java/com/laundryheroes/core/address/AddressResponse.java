package com.laundryheroes.core.address;

public class AddressResponse {

    private final Long id;
    private final String name;
    private final String phone;
    private final String address;
    private final boolean defaultAddress;
    private final Long lat;
    private final Long lng;
    private final String label;

    public AddressResponse(Long id, String name, String phone, String address, boolean defaultAddress,Long lat, Long lng, String label) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.defaultAddress = defaultAddress;
        this.lat = lat;
        this.lng = lng;
        this.label = label;
    }

    public AddressResponse(Address address) {
        this.id = address.getId();
        this.name = address.getName();
        this.phone = address.getPhone();
        this.address = address.getAddress();
        this.defaultAddress = address.isDefault();
        this.lat = address.getLat();
        this.lng = address.getLng();
        this.label = address.getLabel();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isDefaultAddress() { return defaultAddress; }
    public Long getLat() { return lat; }
    public Long getLng() { return lng; }
    public String getLabel() { return label; }
}
