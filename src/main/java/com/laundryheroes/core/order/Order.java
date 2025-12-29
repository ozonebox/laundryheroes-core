package com.laundryheroes.core.order;

import java.time.Instant;
import java.util.List;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.delivery.DeliveryRequest;
import com.laundryheroes.core.pickup.PickupRequest;
import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Address address;

    @ManyToOne(optional = false)
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private String serviceSpeed;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private double totalAmount;


    @Column(nullable = true)
    private String customerPhone;

    @Column(nullable = true)
    private String customerEmail;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant pickupTimeRequested;

    @OneToMany(mappedBy = "order",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<OrderItem> items;

    @OneToOne
    @JoinColumn(name = "pickup_id")
    private PickupRequest pickup;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryRequest delivery;



    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
