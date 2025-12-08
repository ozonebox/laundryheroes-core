// src/main/java/com/laundryheroes/core/order/OrderItemRepository.java
package com.laundryheroes.core.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.laundryService.serviceType, COUNT(oi) " +
           "FROM OrderItem oi " +
           "GROUP BY oi.laundryService.serviceType")
    List<Object[]> breakdownByServiceType();
}
