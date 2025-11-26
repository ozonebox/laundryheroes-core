package com.laundryheroes.core.order;

import java.time.Instant;
import java.util.List;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.address.AddressResponse;
import com.laundryheroes.core.delivery.DeliveryRequest;
import com.laundryheroes.core.pickup.PickupRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private AddressResponse address;
    private OrderStatus status;
    private double totalAmount;
    private double serviceFee;
    private double deliveryFee;
    private Instant createdAt;
    private Instant pickupTimeRequested;
    private List<OrderItemResponse> items;
    private PickupRequest pickupRequest;
    private DeliveryRequest deliveryRequest;
}
