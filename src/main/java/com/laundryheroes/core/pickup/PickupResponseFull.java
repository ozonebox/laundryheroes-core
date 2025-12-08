package com.laundryheroes.core.pickup;

import java.time.Instant;

import com.laundryheroes.core.auth.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickupResponseFull {

    private Long id;
    private Long orderId;
    private Long pickupAddressId;
    private PickupStatus status;
    private Instant scheduledFor;
    private Instant createdAt;
    private UserResponse assignedDriver;
}
