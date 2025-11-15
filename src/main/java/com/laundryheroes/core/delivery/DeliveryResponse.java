package com.laundryheroes.core.delivery;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private Long addressId;
    private DeliveryStatus status;
    private Instant createdAt;
}
