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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTH_FULL')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        return orderService.createOrder(user, request);
    }

    @PostMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<OrderResponse> acceptOrder(@PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<OrderResponse>> myOrders(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return orderService.userOrders(user);
    }
}
