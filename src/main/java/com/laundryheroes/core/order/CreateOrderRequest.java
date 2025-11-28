package com.laundryheroes.core.order;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    private Long addressId;

    @NotNull
    private Long deliveryAddressId;

    @NotNull
    private String serviceSpeed;

    @NotNull
    private String paymentMethod;

    @NotNull
    private Instant pickupTimeRequested;

    @NotNull
    private List<CreateOrderItemRequest> items;
}
