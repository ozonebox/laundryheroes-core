package com.laundryheroes.core.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private static final int ACCESS_COOKIE_MAXAGE = 60 * 15; // 15 mins
    private static final int REFRESH_COOKIE_MAXAGE = 60 * 60 * 24 * 30; // 30 days
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

        if (!response.getResponseCode().equals(ResponseCode.LOGIN_SUCCESS.code()) &&
            !response.getResponseCode().equals(ResponseCode.PROFILE_PENDING.code())) {

            return ResponseEntity.status(200).body(response);
        }
         if (!response.getResponseCode().equals(ResponseCode.SUCCESS.code())) {
            return ResponseEntity.ok(response);
        }

        UserResponse data = response.getData();

        ResponseCookie newAccess = ResponseCookie.from("ACCESS_TOKEN", data.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(86400)
                .build();

        ResponseCookie newRefresh = ResponseCookie.from("REFRESH_TOKEN", data.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(2592000)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", newAccess.toString())
                .header("Set-Cookie", newRefresh.toString())
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
       


        return ResponseEntity.ok()
                .body(response);
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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@RequestBody @Valid RefreshTokenRequest request) {

        ApiResponse<UserResponse> response = userService.refresh(request);

        if (!response.getResponseCode().equals(ResponseCode.SUCCESS.code())) {
            return ResponseEntity.ok(response);
        }



        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestBody @Valid RefreshTokenRequest request) {

        ApiResponse<?> response = userService.logout(request);


        return ResponseEntity.ok()
                .body(response);
    }

    private ResponseCookie buildCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true) // Set to true in production
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }

    private ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

}
