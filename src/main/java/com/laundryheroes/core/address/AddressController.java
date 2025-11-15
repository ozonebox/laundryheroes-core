package com.laundryheroes.core.address;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/address")
@PreAuthorize("hasAuthority('AUTH_FULL')")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ApiResponse<?> add(@Valid @RequestBody AddAddressRequest request, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.addAddress(user, request);
    }

    @PostMapping("/edit")
    public ApiResponse<?> edit(@Valid @RequestBody EditAddressRequest request, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.editAddress(user, request);
    }

    @PostMapping("/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.deleteAddress(user, id);
    }

    @PostMapping("/set-default/{id}")
    public ApiResponse<?> setDefault(@PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.setDefault(user, id);
    }

    @GetMapping("/list")
    public ApiResponse<?> list(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.list(user);
    }
}
