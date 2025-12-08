package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationPublisher notificationPublisher;
    private final UserRepository userRepository;
    private final ResponseFactory responseFactory;

    @PostMapping("/broadcast")
    public ApiResponse<Void> broadcast(
            @Valid @RequestBody AdminBroadcastRequest req
    ) {
        // Very simple example: filter by role
        List<User> users;
        if (req.getRole() != null) {
            users = userRepository.findByRole(req.getRole());
        } else {
            users = userRepository.findAll();
        }

        notificationPublisher.notifyUsers(
            users,
            req.getCategory(),
            NotificationTemplate.ADMIN_BROADCAST,
            Map.of(
                "title", req.getTitle(),
                "body", req.getBody(),
                "meta", req.getMeta()
            )
        );

        // Use ResponseFactory for consistency
        return responseFactory.success(ResponseCode.SUCCESS,null);
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<Void> sendToUser(
        @PathVariable Long userId,
        @Valid @RequestBody AdminUserNotificationRequest req
    ) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        notificationPublisher.notifyUser(
            user,
            req.getCategory(),
            NotificationTemplate.ADMIN_BROADCAST,
            Map.of(
                "title", req.getTitle(),
                "body", req.getBody(),
                "meta", req.getMeta()
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS,null);
    }

    @PostMapping("/users")
    public ApiResponse<Void> sendToUsers(@Valid @RequestBody BulkNotificationRequest req) {

    List<User> users = userRepository.findAllById(req.getUserIds());

    if (users.isEmpty()) {
        return responseFactory.error(ResponseCode.INVALID_REQUEST, "No users found");
    }

    for (User user : users) {
        notificationPublisher.notifyUser(
            user,
            req.getCategory(),
        NotificationTemplate.ADMIN_BROADCAST,
        Map.of(
            "title", req.getTitle(),
            "body", req.getBody(),
            "meta", req.getMeta()
        )
    );
}

    return responseFactory.success(ResponseCode.SUCCESS, null);
}


}
