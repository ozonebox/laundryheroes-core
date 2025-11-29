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
    public ApiResponse<?> add(@Valid @RequestBody CreateCartRequest request,
                              Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.addToCart(user, request);
    }

  

    @DeleteMapping("/clear")
    public ApiResponse<?> clear(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.clearCart(user);
    }

    @GetMapping
    public ApiResponse<List<CartResponse>> cart(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.getCart(user);
    }
}
