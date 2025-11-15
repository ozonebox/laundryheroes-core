package com.laundryheroes.core.pickup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;

public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {

    List<PickupRequest> findByAssignedDriver(User driver);
}
