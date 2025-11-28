package com.laundryheroes.core.websocket;

import java.util.Map;

import com.laundryheroes.core.auth.UserService;
import com.laundryheroes.core.common.*;
import com.laundryheroes.core.order.OrderProcessingService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/bootstrap")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
@RequiredArgsConstructor
public class AdminBootstrapController {

    //private final AdminDashboardService dashboardService;
    private final ResponseFactory responseFactory;
    private final OrderProcessingService processingService;
     private final UserService userService;

    @GetMapping
    public ApiResponse<Map<String, Object>> bootstrap() {

        Map<String, Object> data = Map.of(
            "orders", processingService.allOrders(),
            "users", userService.allUsers()
        );

        return responseFactory.success(ResponseCode.SUCCESS, data);
    }
}
