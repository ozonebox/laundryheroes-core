package com.laundryheroes.core.driver;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.pickup.PickupRequest;
import com.laundryheroes.core.pickup.PickupResponse;
import com.laundryheroes.core.pickup.PickupService;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver/pickups")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class DriverPickupController {

    private final DriverPickupService driverPickupService;

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<List<PickupResponse>> assignedPickups(Authentication auth) {
        User driver = (User) auth.getPrincipal();
        return driverPickupService.getAssignedPickups(driver);
    }

    @GetMapping("/{pickupId}")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> pickupDetails(
            @PathVariable Long pickupId,
            Authentication auth
    ) {
        User driver = (User) auth.getPrincipal();
        return driverPickupService.getPickupDetails(driver, pickupId);
    }

    @PostMapping("/{pickupId}/status")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<PickupResponse> updatePickupStatus(
            @PathVariable Long pickupId,
            @RequestParam String status,
            Authentication auth
    ) {
        User driver = (User) auth.getPrincipal();
        return driverPickupService.updatePickupStatus(driver, pickupId, status);
    }
}
