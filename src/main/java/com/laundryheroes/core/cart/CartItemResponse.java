package com.laundryheroes.core.cart;

import com.laundryheroes.core.servicecatalog.ServiceType;
import com.laundryheroes.core.servicecatalog.ItemType;
import com.laundryheroes.core.servicecatalog.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long serviceId;
    private ServiceType serviceType;
    private ItemType itemType;
    private Category category;

    private double price;
    private int quantity;
    private double subtotal;
}
