package com.laundryheroes.core.order;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders/processing")
@PreAuthorize("hasAuthority('AUTH_FULL') and hasAnyRole('ADMIN','SUPERADMIN')")
@RequiredArgsConstructor
public class OrderProcessingController {

    private final OrderProcessingService processingService;

    @PostMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return processingService.updateStatus(orderId, request.getStatus());
    }


     @PostMapping("/cancel/{orderId}")
    public ApiResponse<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        return processingService.adminCancelOrder(user, orderId);
    }

    @GetMapping("/all")
    public ApiResponse<List<OrderResponse>> allOrders(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return processingService.allOrders();
    }
}
