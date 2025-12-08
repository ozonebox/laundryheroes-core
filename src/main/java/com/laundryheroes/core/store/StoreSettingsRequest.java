package com.laundryheroes.core.store;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreSettingsRequest {

    @NotNull
    private Boolean isOpen;
}

