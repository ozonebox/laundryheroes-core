package com.laundryheroes.core.pickup;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pickups")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class PickupController {

    private final PickupService pickupService;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> createPickup(@PathVariable Long orderId) {
        return pickupService.createPickup(orderId);
    }

    @PostMapping("/{pickupId}/assign-driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> assignDriver(@PathVariable Long pickupId,
                                                    @PathVariable Long driverId) {
        return pickupService.assignDriver(pickupId, driverId);
    }

    @PostMapping("/{pickupId}/change-driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> changeDriver(@PathVariable Long pickupId,
                                                    @PathVariable Long driverId) {
        return pickupService.changeDriver(pickupId, driverId);
    }

    @PostMapping("/{pickupId}/unassign-driver")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> unassignDriver(@PathVariable Long pickupId) {
        return pickupService.unassignDriver(pickupId);
    }

    @PostMapping("/{pickupId}/driver/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ApiResponse<PickupResponse> driverUpdateStatus(@PathVariable Long pickupId,
                                                          @RequestParam PickupStatus status,
                                                          Authentication auth) {

        User driver = (User) auth.getPrincipal();
        return pickupService.driverUpdateStatus(driver, pickupId, status);
    }

    @PostMapping("/{pickupId}/admin/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> adminUpdateStatus(@PathVariable Long pickupId,
                                                         @RequestParam PickupStatus status) {
        return pickupService.updateStatus(pickupId, status);
    }
}
