package com.laundryheroes.core.store;


import com.laundryheroes.core.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class StoreSettingsController {

    private final StoreSettingsService service;

    @GetMapping("/store")
    public ApiResponse<StoreSettings> getSettings() {
        return service.get();
    }

    @PostMapping("/store")
    public ApiResponse<StoreSettings> updateSettings(
            @Valid @RequestBody StoreSettingsRequest req
    ) {
        return service.update(req);
    }
}
