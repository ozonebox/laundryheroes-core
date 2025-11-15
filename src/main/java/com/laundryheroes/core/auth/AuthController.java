package com.laundryheroes.core.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.user.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegisterRequest request) {
        ApiResponse<UserResponse> response = userService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<UserResponse> response = userService.login(request);
        if (!response.getResponseCode().equals(ResponseCode.LOGIN_SUCCESS.code())&&!response.getResponseCode().equals(ResponseCode.PROFILE_PENDING.code())) {
            return ResponseEntity.status(200).body(response);
        }

        UserResponse user = response.getData();

        String token = user.getToken();

        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", token)
                .httpOnly(true)
                .secure(false) // change to true in production HTTPS
                .path("/")
                .maxAge(86400) // 1 day
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }
    
    @PostMapping("/verify-email")
    @PreAuthorize("hasAuthority('AUTH_PENDING')")
    public ResponseEntity<ApiResponse<VerifyEmailResponse>> verifyEmail(@RequestBody @Valid VerifyEmailRequest request,org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();
        ApiResponse<VerifyEmailResponse> response = userService.sendVerificationOtp(user,request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate-user")
    @PreAuthorize("hasAuthority('AUTH_PENDING')")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(@RequestBody @Valid ActivateUserRequest request,org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();
        ApiResponse<UserResponse> response = userService.activateUser(user,request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordInitResponse>> resetPasswordInit(@RequestBody @Valid ResetPasswordInitRequest request) {
        ApiResponse<ResetPasswordInitResponse> response = userService.resetPasswordInit(request);
        return ResponseEntity.ok(response);
    }

     @PostMapping("/reset-password-complete")
    public ResponseEntity<ApiResponse<UserResponse>> resetPasswordComplete(@RequestBody @Valid ResetPasswordCompleteRequest request) {
        ApiResponse<UserResponse> response = userService.resetPasswordComplete(request);
        return ResponseEntity.ok(response);
    }

}
