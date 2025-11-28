package com.laundryheroes.core.notification;

import java.util.List;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications/logs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class NotificationLogController {

    private final NotificationLogService logService;
    private final ResponseFactory responseFactory;

    @GetMapping("/push/latest")
    public ApiResponse<List<NotificationLogResponse>> latestPush() {

        return responseFactory.success(
                ResponseCode.SUCCESS,
                logService.getLatestPushNotifications()
        );
    }
}
