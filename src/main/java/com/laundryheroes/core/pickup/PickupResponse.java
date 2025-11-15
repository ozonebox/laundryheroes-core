package com.laundryheroes.core.pickup;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PickupResponse {

    private Long id;
    private Long orderId;
    private Long pickupAddressId;
    private PickupStatus status;
    private Instant scheduledFor;
    private Instant createdAt;
}
