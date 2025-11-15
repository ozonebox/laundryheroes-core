package com.laundryheroes.core.driver;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.pickup.*;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverPickupService {

    private final PickupRequestRepository pickupRepo;
    private final PickupService pickupService;
    private final ResponseFactory responseFactory;

    public ApiResponse<List<PickupResponse>> getAssignedPickups(User driver) {

        List<PickupResponse> pickups = pickupRepo.findByAssignedDriver(driver)
                .stream()
                .map(p -> new PickupResponse(
                        p.getId(),
                        p.getOrder().getId(),
                        p.getPickupAddress().getId(),
                        p.getStatus(),
                        p.getScheduledFor(),
                        p.getCreatedAt()
                )).toList();

        return responseFactory.success(ResponseCode.SUCCESS, pickups);
    }

    public ApiResponse<PickupResponse> getPickupDetails(User driver, Long pickupId) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);

        if (pickup == null || pickup.getAssignedDriver() == null || !pickup.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        return responseFactory.success(ResponseCode.SUCCESS, pickupService.toResponseDirect(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> updatePickupStatus(User driver, Long pickupId, String newStatus) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);

        if (pickup == null || pickup.getAssignedDriver() == null || !pickup.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        PickupStatus status = PickupStatus.valueOf(newStatus.toUpperCase());

        return pickupService.updateStatus(pickupId, status);
    }
}
