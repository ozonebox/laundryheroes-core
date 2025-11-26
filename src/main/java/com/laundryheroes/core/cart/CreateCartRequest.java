package com.laundryheroes.core.cart;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartRequest {

    @NotNull
    private String email;

    @NotNull
    private List<CreateCartItemRequest> items;
}
