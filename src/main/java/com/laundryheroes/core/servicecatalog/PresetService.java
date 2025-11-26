package com.laundryheroes.core.servicecatalog;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresetService {

    private final ResponseFactory responseFactory;

    public PresetService(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    public ApiResponse<List<PresetResponse>> getPresetServices() {

        List<PresetResponse> list = List.of(
            new PresetResponse(
                1,
                "Basic Shirt Package",
                "Weekly shirt wash & iron package.",
                "MEN",
                true,
                List.of(
                    new PresetItem(2, 5),
                    new PresetItem(1, 5)
                ),
                6000
            ),
            new PresetResponse(
                2,
                "Premium Suit Care",
                "Professional dry clean & ironing for suits.",
                "MEN",
                true,
                List.of(
                    new PresetItem(21, 2),
                    new PresetItem(19, 2)
                ),
                7600
            ),
            new PresetResponse(
                3,
                "Family Clothing Pack",
                "A mixed bundle for regular weekly laundry.",
                "FAMILY",
                true,
                List.of(
                    new PresetItem(2, 6),
                    new PresetItem(8, 6),
                    new PresetItem(37, 6)
                ),
                12600
            ),
            new PresetResponse(
                4,
                "Women's Premium Dress Care",
                "Dry cleaning and ironing for delicate dresses.",
                "WOMEN",
                true,
                List.of(
                    new PresetItem(51, 3),
                    new PresetItem(49, 3)
                ),
                8400
            ),
            new PresetResponse(
                5,
                "Home Essentials Pack",
                "Laundry care for towels and bed sheets.",
                "HOME",
                true,
                List.of(
                    new PresetItem(104, 4),
                    new PresetItem(109, 3)
                ),
                6100
            ),
            new PresetResponse(
                6,
                "Kids School Pack",
                "Weekly wash & dry for school uniforms.",
                "KIDS",
                true,
                List.of(
                    new PresetItem(74, 5),
                    new PresetItem(80, 5)
                ),
                6250
            ),
            new PresetResponse(
                7,
                "Premium Complete Care",
                "Luxury multi-item special care treatment.",
                "MEN",
                true,
                List.of(
                    new PresetItem(6, 3),
                    new PresetItem(24, 3),
                    new PresetItem(30, 3)
                ),
                24600
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }
}
