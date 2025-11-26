package com.laundryheroes.core.servicecatalog;

import java.util.List;

public class PresetResponse {
    private int id;
    private String name;
    private String description;
    private String category;
    private boolean active;
    private List<PresetItem> items;
    private double totalPrice;

    public PresetResponse(
            int id,
            String name,
            String description,
            String category,
            boolean active,
            List<PresetItem> items,
            double totalPrice
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.active = active;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public List<PresetItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
}
