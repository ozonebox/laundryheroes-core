package com.laundryheroes.core.admin.notification;

import com.laundryheroes.core.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notification/templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    // ------------------------------------------------------
    // CREATE TEMPLATE (SUPERADMIN ONLY)
    // ------------------------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ApiResponse<?> create(@RequestBody NotificationTemplateRequest req) {
        return service.create(req);
    }

    // ------------------------------------------------------
    // UPDATE TEMPLATE
    // ------------------------------------------------------
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ApiResponse<?> update(
            @PathVariable Long id,
            @RequestBody NotificationTemplateRequest req
    ) {
        return service.update(id, req);
    }

    // ------------------------------------------------------
    // DELETE TEMPLATE
    // ------------------------------------------------------
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ApiResponse<?> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    // ------------------------------------------------------
    // LIST ACTIVE TEMPLATES (ADMIN + SUPERADMIN)
    // ------------------------------------------------------
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<?> active() {
        return service.listActive();
    }

    // ------------------------------------------------------
    // GET SINGLE TEMPLATE
    // ------------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<?> get(@PathVariable Long id) {
        return service.get(id);
    }
}
