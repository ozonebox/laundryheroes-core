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

            // 1️⃣ Men's thob weekly care
            new PresetResponse(
                1,
                "Men's Thob Weekly",
                "Weekly wash & press for thobs plus ghotra pressing.",
                "MEN",
                true,
                List.of(
                    // THOB – WASH_PRESS NORMAL (id=4) ×5
                    new PresetItem(86, 5),
                    // GHOTRA – PRESS NORMAL (id=18) ×5
                    new PresetItem(99, 5)
                ),
                45 // 5×5 + 5×4 = 25 + 20 = 45
            ),

            // 2️⃣ Men's occasion bisht & jacket
            new PresetResponse(
                2,
                "Men's Occasion Pack",
                "Full care for bisht, jacket and matching ghotra.",
                "MEN",
                true,
                List.of(
                    // BISHT – DRY_CLEAN NORMAL (id=20) ×1
                    new PresetItem(100, 1),
                    // JACKET – DRY_CLEAN NORMAL (id=26) ×1
                    new PresetItem(106, 1),
                    // GHOTRA – DRY_CLEAN NORMAL (id=14) ×1
                    new PresetItem(94, 1),
                    // THOB_WOOL – DRY_CLEAN NORMAL (id=8) ×1
                    new PresetItem(87, 1)
                ),
                68 // 30 + 15 + 8 + 15 = 68
            ),

            // 3️⃣ Women's abaya & blouse weekly
            new PresetResponse(
                3,
                "Women's Everyday Pack",
                "Regular wash & press for abayas and blouses.",
                "WOMEN",
                true,
                List.of(
                    // ABAYA – WASH_PRESS NORMAL (id=34) ×4
                    new PresetItem(114, 4),
                    // BLOUSE – WASH_PRESS NORMAL (id=40) ×4
                    new PresetItem(120, 4)
                ),
                80 // 4×15 + 4×5 = 60 + 20 = 80
            ),

            // 4️⃣ Wedding dress full care
            new PresetResponse(
                4,
                "Wedding Dress Full Care",
                "Professional dry clean and pressing for one wedding dress.",
                "WOMEN",
                true,
                List.of(
                    // WEDDING_DRESS – DRY_CLEAN NORMAL (id=44) ×1
                    new PresetItem(124, 1),
                    // WEDDING_DRESS – PRESS NORMAL (id=46) ×1
                    new PresetItem(126, 1)
                ),
                300 // 200 + 100 = 300
            ),

            // 5️⃣ Sari party pack
            new PresetResponse(
                5,
                "Sari Party Pack",
                "Dry clean and press for multiple special saris.",
                "WOMEN",
                true,
                List.of(
                    // SPECIAL_SARI – DRY_CLEAN NORMAL (id=48) ×3
                    new PresetItem(128, 3),
                    // SPECIAL_SARI – PRESS NORMAL (id=52) ×3
                    new PresetItem(132, 3)
                ),
                156 // 3×40 + 3×12 = 120 + 36 = 156
            ),

            // 6️⃣ Kids school uniforms
            new PresetResponse(
                6,
                "Kids School Uniform Pack",
                "Weekly wash & press for children's school uniforms.",
                "KIDS",
                true,
                List.of(
                    // THOB (KIDS) – WASH_PRESS NORMAL (id=56) ×5
                    new PresetItem(136, 5),
                    // DRESS (KIDS) – WASH_PRESS NORMAL (id=62) ×3
                    new PresetItem(142, 3)
                ),
                79 // 5×5 + 3×18 = 25 + 54 = 79
            ),

            // 7️⃣ Home bedding refresh
            new PresetResponse(
                7,
                "Home Bedding Refresh",
                "Freshen bed sheets and pillows in one go.",
                "HOME",
                true,
                List.of(
                    // BED_SHEET – WASH_PRESS NORMAL (id=74) ×4
                    new PresetItem(154, 4),
                    // PILLOW – WASH_PRESS NORMAL (id=86) ×4
                    new PresetItem(166, 4)
                ),
                64 // 4×10 + 4×6 = 40 + 24 = 64
            ),

            // 8️⃣ Curtains & bedding combo
            new PresetResponse(
                8,
                "Curtains & Bedding Combo",
                "Deep clean for curtains plus regular sheets.",
                "HOME",
                true,
                List.of(
                    // CURTAINS_BIG – WASH_PRESS NORMAL (id=80) ×3
                    new PresetItem(160, 3),
                    // BED_SHEET – WASH_PRESS NORMAL (id=74) ×3
                    new PresetItem(154, 3)
                ),
                99 // 3×23 + 3×10 = 69 + 30 = 99
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }
}
