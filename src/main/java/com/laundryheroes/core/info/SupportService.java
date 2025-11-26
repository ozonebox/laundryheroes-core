package com.laundryheroes.core.info;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {
        private final ResponseFactory responseFactory;
    public ApiResponse<?> getSupportInfo() {

        // Contact info
        String whatsapp = "+2348012345678";
        String phone = "+2348012345678";

        // FAQs
        List<FaqItem> faqs = List.of(
            new FaqItem(
                "How does Laundry Heroes work?",
                "Schedule a pickup, we clean your items, and deliver them back to you."
            ),
            new FaqItem(
                "How long does laundry take?",
                "Standard orders take 24–48 hours. Express services are faster."
            ),
            new FaqItem(
                "What if I'm not available?",
                "Our rider will contact you. Unsuccessful attempts may be rescheduled."
            ),
            new FaqItem(
                "How is pricing calculated?",
                "Pricing depends on item type, service selected, and quantity."
            ),
            new FaqItem(
                "What if an item is damaged or lost?",
                "Report within 24 hours for investigation and resolution."
            )
        );

        // ✅ Terms
        LegalContent terms = new LegalContent(
            "v1.0",
            "2025-01-01",
            TERMS_TEXT
        );

        // ✅ Privacy
        LegalContent privacy = new LegalContent(
            "v1.0",
            "2025-01-01",
            PRIVACY_TEXT
        );

        SupportInfoResponse data = new SupportInfoResponse(
            whatsapp,
            phone,
            faqs,
            terms,
            privacy
        );
        return responseFactory.success(ResponseCode.ORDER_SUCCESS, data);
       
    }

    // -------------------------
    // STATIC CONTENT
    // -------------------------

    private static final String TERMS_TEXT = """
Laundry Heroes – Terms & Conditions

By using Laundry Heroes, you agree to the following terms.

1. Services
Laundry Heroes provides on-demand laundry pickup and delivery.

2. Orders & Payments
Orders must be confirmed through the app. Prices may change based on item count.

3. Pickups & Deliveries
You are responsible for availability during pickup and delivery.

4. Item Care & Liability
We are not responsible for normal wear and pre-existing damage.

5. Prohibited Items
Do not include valuables or hazardous items.

6. Cancellations
Orders may be cancelled before pickup only.

7. Account Responsibility
You are responsible for your account activities.

8. Policy Changes
We may update these terms at any time.
""";

    private static final String PRIVACY_TEXT = """
Laundry Heroes – Privacy Policy

We respect your privacy.

1. Data We Collect
Name, contact details, addresses, and order information.

2. How We Use Data
To provide laundry services and customer support.

3. Data Sharing
Only with service partners when required.

4. Data Security
We use industry best practices to protect your data.

5. Location Data
Used strictly for delivery purposes.

6. Your Rights
Request access or deletion of your data anytime.
""";
}