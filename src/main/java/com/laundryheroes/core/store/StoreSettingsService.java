package com.laundryheroes.core.store;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class StoreSettingsService {

    private final StoreSettingsRepository repo;
    private final ResponseFactory responseFactory;

    @PostConstruct
    public void init() {
        // Ensure exactly 1 row always exists
        if (repo.count() == 0) {
            StoreSettings s = new StoreSettings();
            s.setOpen(true);
            repo.save(s);
        }
    }

    public ApiResponse<StoreSettings> get() {
        StoreSettings settings = repo.findAll().stream().findFirst().orElse(null);

        if (settings == null) {
            return responseFactory.error(ResponseCode.NOT_FOUND);
        }

        return responseFactory.success(ResponseCode.SUCCESS, settings);
    }

    public ApiResponse<StoreSettings> update(StoreSettingsRequest req) {
        StoreSettings settings = repo.findAll().stream().findFirst().orElse(null);

        if (settings == null) {
            return responseFactory.error(ResponseCode.NOT_FOUND);
        }

        settings.setOpen(req.getIsOpen());
        repo.save(settings);

        return responseFactory.success(ResponseCode.SUCCESS, settings);
    }
    public StoreSettings getRaw() {
        return repo.findAll().get(0);
    }

}
