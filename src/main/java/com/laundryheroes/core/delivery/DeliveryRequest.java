package com.laundryheroes.core.delivery;

import java.time.Instant;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.order.Order;
import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_requests")
@Data
@NoArgsConstructor
public class DeliveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private Address deliveryAddress;

    @ManyToOne
    private User assignedDriver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.REQUESTED;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}
