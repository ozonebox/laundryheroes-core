package com.laundryheroes.core.cart;

import java.time.Instant;
import java.util.List;

import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "cart",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<CartItem> items;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

}
