package com.laundryheroes.core.order;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders/processing")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class OrderProcessingController {

    private final OrderProcessingService processingService;

    @PostMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return processingService.updateStatus(orderId, request.getStatus());
    }
}
