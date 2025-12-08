package com.laundryheroes.core.pickup;

import java.time.Instant;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.order.Order;
import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pickup_requests")
@Data
@NoArgsConstructor
public class PickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private Address pickupAddress;

    @ManyToOne(optional = true)
    private User assignedDriver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickupStatus status = PickupStatus.REQUESTED;

    @Column(nullable = false)
    private Instant scheduledFor;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}
