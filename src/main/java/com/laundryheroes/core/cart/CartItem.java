package com.laundryheroes.core.cart;

import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.user.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "service_id"}))
@Data
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private LaundryService service;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double subtotal;
}
