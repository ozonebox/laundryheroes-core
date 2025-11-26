package com.laundryheroes.core.address;

import com.laundryheroes.core.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String label;

    @NotNull
    private Long lng;

    @NotNull
    private Long lat;

    private boolean isDefault;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isDefault() { return isDefault; }
    public Long getLng() { return lng; }
    public Long getLat() { return lat; }
    public String getLabel() { return label; }

    public void setUser(User user) { this.user = user; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
    public void setLong(Long lng) { this.lng = lng; }
    public void setLat(Long lat) { this.lat = lat; }
    public void setLabel(String label) { this.label = label; }
}
