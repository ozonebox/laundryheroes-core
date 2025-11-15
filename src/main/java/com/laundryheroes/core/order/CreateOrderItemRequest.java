package com.laundryheroes.core.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItemRequest {

    @NotNull
    private Long serviceId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
