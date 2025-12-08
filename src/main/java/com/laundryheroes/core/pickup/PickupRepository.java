package com.laundryheroes.core.pickup;

import com.laundryheroes.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PickupRepository extends JpaRepository<PickupRequest, Long> {

    @Query("""
    SELECT COUNT(p) FROM PickupRequest p
    WHERE p.assignedDriver = :driver
    AND p.status NOT IN ('PICKED_UP','DELIVERED_TO_STORE')
""")
long countActivePickupsForDriver(User driver);

}
