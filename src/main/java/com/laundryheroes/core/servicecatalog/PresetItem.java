package com.laundryheroes.core.servicecatalog;

public class PresetItem {
    private int serviceId;
    private int quantity;

    public PresetItem(int serviceId, int quantity) {
        this.serviceId = serviceId;
        this.quantity = quantity;
    }

    public int getServiceId() { return serviceId; }
    public int getQuantity() { return quantity; }
}
