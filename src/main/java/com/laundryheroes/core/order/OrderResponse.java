package com.laundryheroes.core.order;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long addressId;
    private OrderStatus status;
    private double totalAmount;
    private Instant createdAt;
    private Instant pickupTimeRequested;
    private List<OrderItemResponse> items;
}
