package com.laundryheroes.core.delivery;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deliveries")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> createDelivery(@PathVariable Long orderId) {
        return deliveryService.createDelivery(orderId);
    }

    @PostMapping("/{deliveryId}/assign-driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> assignDriver(@PathVariable Long deliveryId,
                                                      @PathVariable Long driverId) {
        return deliveryService.assignDriver(deliveryId, driverId);
    }

    @PostMapping("/{deliveryId}/change-driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> changeDriver(@PathVariable Long deliveryId,
                                                      @PathVariable Long driverId) {
        return deliveryService.changeDriver(deliveryId, driverId);
    }

    @PostMapping("/{deliveryId}/unassign-driver")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> unassignDriver(@PathVariable Long deliveryId) {
        return deliveryService.unassignDriver(deliveryId);
    }

    @PostMapping("/{deliveryId}/admin/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<DeliveryResponse> adminUpdateStatus(@PathVariable Long deliveryId,
                                                           @RequestParam DeliveryStatus status) {
        return deliveryService.adminUpdateStatus(deliveryId, status);
    }
}
