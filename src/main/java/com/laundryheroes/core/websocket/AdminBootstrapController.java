package com.laundryheroes.core.websocket;

import java.util.Map;

import com.laundryheroes.core.account.AccountService;
import com.laundryheroes.core.account.AdminEditProfileRequest;
import com.laundryheroes.core.admin.dashboard.AdminDashboardService;
import com.laundryheroes.core.admin.notification.NotificationTemplateService;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.auth.UserService;
import com.laundryheroes.core.common.*;
import com.laundryheroes.core.order.OrderProcessingService;
import com.laundryheroes.core.store.StoreSettingsService;
import com.laundryheroes.core.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
@RequiredArgsConstructor
public class AdminBootstrapController {

    //private final AdminDashboardService dashboardService;
    private final ResponseFactory responseFactory;
    private final OrderProcessingService processingService;
     private final UserService userService;
     private final AccountService accountService;
     private final AdminDashboardService adminDashboardService;
    private final NotificationTemplateService notificationTemplateService;
    private final StoreSettingsService storeSettingsService;
    @GetMapping("/bootstrap")
    public ApiResponse<Map<String, Object>> bootstrap(org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();
        Map<String, Object> data = Map.of(
            "data", accountService.me(user),
            "orders", processingService.allOrders(),
            "users", userService.allUsers(),
            "dashboard",adminDashboardService.getDashboard(),
            "notificationTemplates",notificationTemplateService.listActive(),
            "storeSettings",storeSettingsService.get()
        );

        return responseFactory.success(ResponseCode.SUCCESS, data);
    }

    @PostMapping("/users/update-profile")
    public ApiResponse<UserResponse> editProfile(@RequestBody @Valid AdminEditProfileRequest request,org.springframework.security.core.Authentication auth) {
        User user = (User) auth.getPrincipal();

        return accountService.adminEditProfile(user,request);
    }
}
