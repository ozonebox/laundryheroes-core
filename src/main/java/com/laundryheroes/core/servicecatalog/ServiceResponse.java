package com.laundryheroes.core.servicecatalog;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceResponse {

    private Long id;
    private ServiceType serviceType;
    private ItemType itemType;
    private Speed speed;
    private Category category;
    private double price;
    private boolean active;
}
