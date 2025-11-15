package com.laundryheroes.core.driver;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.delivery.*;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver/deliveries")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class DriverDeliveryController {

    private final DriverDeliveryService driverDeliveryService;

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<List<DeliveryResponse>> assignedDeliveries(Authentication auth) {
        User driver = (User) auth.getPrincipal();
        return driverDeliveryService.getAssignedDeliveries(driver);
    }

    @GetMapping("/{deliveryId}")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> deliveryDetails(
            @PathVariable Long deliveryId,
            Authentication auth
    ) {
        User driver = (User) auth.getPrincipal();
        return driverDeliveryService.getDeliveryDetails(driver, deliveryId);
    }

    @PostMapping("/{deliveryId}/status")
    @PreAuthorize("hasRole('DRIVER','ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestParam String status,
            Authentication auth
    ) {
        User driver = (User) auth.getPrincipal();
        return driverDeliveryService.updateDeliveryStatus(driver, deliveryId, status);
    }
}
