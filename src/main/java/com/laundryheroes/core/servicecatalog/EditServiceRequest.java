package com.laundryheroes.core.servicecatalog;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditServiceRequest {

    private ServiceType serviceType;
    private ItemType itemType;
    private Category category;
    private Speed speed;

    @NotNull
    private Double price;

    private Boolean active;
}
