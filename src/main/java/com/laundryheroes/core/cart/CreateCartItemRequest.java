package com.laundryheroes.core.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemRequest {

    @NotNull
    private Long serviceId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
