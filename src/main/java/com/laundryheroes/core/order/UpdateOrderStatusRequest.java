package com.laundryheroes.core.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status;
}
