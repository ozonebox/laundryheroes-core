package com.laundryheroes.core.notification;

import java.time.Instant;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications/devices")
@PreAuthorize("hasAuthority('AUTH_FULL')")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceRepository deviceRepo;
    private final ResponseFactory responseFactory;

    @PostMapping
    @Transactional
    public ApiResponse<Void> registerDevice(
            @Valid @RequestBody UserDeviceRequest req,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();

        deviceRepo.deactivateAllByUserAndPlatform(user,req.getPlatform());

        // Deactivate any previous records for this token
        deviceRepo.deactivateByToken(req.getDeviceToken());

        UserDevice device = new UserDevice();
        device.setUser(user);
        device.setDeviceToken(req.getDeviceToken());
        device.setPlatform(req.getPlatform());
        device.setActive(true);
        device.setLastSeenAt(Instant.now());

        deviceRepo.save(device);

        return responseFactory.success(ResponseCode.SUCCESS,null);
    }
}
