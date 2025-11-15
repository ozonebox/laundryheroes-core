package com.laundryheroes.core.cart;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('AUTH_FULL')")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<?> add(@Valid @RequestBody AddToCartRequest request,
                              Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.addToCart(user, request);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCartRequest request,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        return service.updateCartItem(user, id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<?> remove(@PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.removeItem(user, id);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<?> clear(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.clearCart(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<CartItemResponse>> cart(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.getCart(user);
    }
}
