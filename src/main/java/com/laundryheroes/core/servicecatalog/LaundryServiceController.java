package com.laundryheroes.core.servicecatalog;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.laundryheroes.core.common.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/services")
@PreAuthorize("hasAuthority('AUTH_FULL')")
public class LaundryServiceController {

    private final LaundryServiceService service;

    public LaundryServiceController(LaundryServiceService service) {
        this.service = service;
    }

    @GetMapping("/active")
    public ApiResponse<List<ServiceResponse>> activeServices() {
        return service.getActiveServices();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<List<ServiceResponse>> allServices() {
        return service.getAllServices();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<ServiceResponse> add(@Valid @RequestBody AddServiceRequest request) {
        return service.addService(request);
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<ServiceResponse> edit(
            @PathVariable Long id,
            @Valid @RequestBody EditServiceRequest request
    ) {
        return service.editService(id, request);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ApiResponse<?> delete(@PathVariable Long id) {
        return service.deleteService(id);
    }
}
