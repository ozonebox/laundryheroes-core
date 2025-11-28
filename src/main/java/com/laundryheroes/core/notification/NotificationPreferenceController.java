package com.laundryheroes.core.notification;

import java.util.List;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications/preferences")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService prefService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ApiResponse<List<NotificationPreferenceDto>> getMyPreferences(
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        List<NotificationPreferenceDto> prefs =
                prefService.getPreferencesForUser(user);

        return responseFactory.success(ResponseCode.SUCCESS, prefs);
    }

    @PostMapping
    public ApiResponse<List<NotificationPreferenceDto>> updateMyPreferences(
            Authentication auth,
            @RequestBody List<NotificationPreferenceDto> request
    ) {
        User user = (User) auth.getPrincipal();
        List<NotificationPreferenceDto> updated =
                prefService.updatePreferences(user, request);

        return responseFactory.success(ResponseCode.SUCCESS, updated);
    }
}
