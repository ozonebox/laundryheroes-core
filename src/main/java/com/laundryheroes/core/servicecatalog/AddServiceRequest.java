package com.laundryheroes.core.servicecatalog;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddServiceRequest {

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private ItemType itemType;

    @NotNull
    private Category category;

    @NotNull
    private Double price;
}
