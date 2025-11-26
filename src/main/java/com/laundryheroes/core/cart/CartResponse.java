package com.laundryheroes.core.cart;


import java.time.Instant;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Instant updatedAt;
    private double totalAmount;
    private List<CartItemResponse> items;
    
    
}
