// src/main/java/com/laundryheroes/core/delivery/DeliveryRepository.java
package com.laundryheroes.core.delivery;

import com.laundryheroes.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<DeliveryRequest, Long> {

    @Query("SELECT COUNT(d) FROM DeliveryRequest d " +
           "WHERE d.assignedDriver = :driver " +
           "AND d.status <> 'DELIVERED'")
    long countActiveDeliveriesForDriver(User driver);
}
