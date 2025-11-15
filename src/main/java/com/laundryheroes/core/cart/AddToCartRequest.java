package com.laundryheroes.core.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull
    private Long serviceId;

    @NotNull
    private Integer quantity;
}
