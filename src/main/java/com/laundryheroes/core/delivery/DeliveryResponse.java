package com.laundryheroes.core.delivery;

import java.time.Instant;

import com.laundryheroes.core.auth.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private Long addressId;
    private DeliveryStatus status;
    private Instant createdAt;
}
