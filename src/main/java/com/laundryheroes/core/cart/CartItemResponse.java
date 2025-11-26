package com.laundryheroes.core.cart;

import com.laundryheroes.core.servicecatalog.Category;
import com.laundryheroes.core.servicecatalog.ItemType;
import com.laundryheroes.core.servicecatalog.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long serviceId;
    private ServiceType serviceType;
    private ItemType itemType;
    private Category category;
    private double unitPrice;
    private int quantity;
    private double subtotal;
}
