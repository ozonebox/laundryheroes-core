package com.laundryheroes.core.account;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.user.User;

import jakarta.validation.Valid;

@RestController
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();

        return accountService.me(user);
    }

    @PostMapping("/edit-profile")
    public ApiResponse<UserResponse> editProfile(@RequestBody @Valid EditProfileRequest request,org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();

        return accountService.editProfile(user,request);
    }

    @PostMapping("/change-password")
    public ApiResponse<UserResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request,org.springframework.security.core.Authentication auth) {
         User user = (User) auth.getPrincipal();
        return accountService.changePassword(user,request);
    }
}

