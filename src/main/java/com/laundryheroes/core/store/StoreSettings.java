package com.laundryheroes.core.store;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "store_settings")
@Data
public class StoreSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // true = store is accepting orders
    private boolean isOpen = true;
}
