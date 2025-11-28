package com.laundryheroes.core.notification;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationTemplateResolver implements NotificationTemplateResolver {

    public NotificationMessage resolve(
            NotificationTemplate template,
            Map<String, Object> ctx
    ) {
        return switch (template) {

            // ─────────────────────────────
            // ORDER LIFECYCLE
            // ─────────────────────────────
            case ORDER_CREATED -> new NotificationMessage(
                "Order placed successfully",
                "Your order #" + v(ctx, "orderId")
                    + " has been created successfully."
            );

            case ORDER_ACCEPTED -> new NotificationMessage(
                "Order accepted",
                "Good news! Your order #" + v(ctx, "orderId")
                    + " has been accepted by Laundry Heroes and will be processed shortly."
            );

            case ORDER_PICKUP_STARTED -> new NotificationMessage(
                "Pickup on the way",
                "Your pickup for order #" + v(ctx, "orderId")
                    + " has started. A rider will be on the way soon."
            );

            case ORDER_RECEIVED -> new NotificationMessage(
                "Order received at store",
                "Your order #" + v(ctx, "orderId")
                    + " has arrived at our facility and is queued for cleaning."
            );

            case ORDER_IN_PROGRESS -> new NotificationMessage(
                "Laundry in progress",
                "Your order #" + v(ctx, "orderId")
                    + " is currently being processed (" + v(ctx, "stage") + ")."
            );

            case ORDER_READY -> new NotificationMessage(
                "Order ready",
                "Your order #" + v(ctx, "orderId")
                    + " is ready. We’ll arrange delivery to your address shortly."
            );

            case ORDER_OUT_FOR_DELIVERY -> new NotificationMessage(
                "Order out for delivery",
                "Your order #" + v(ctx, "orderId")
                    + " is out for delivery. Please keep your phone close."
            );

            case ORDER_COMPLETED -> new NotificationMessage(
                "Order completed",
                "Your order #" + v(ctx, "orderId")
                    + " has been delivered. Thank you for using Laundry Heroes!"
            );

            case ORDER_CANCELLED -> new NotificationMessage(
                "Order cancelled",
                buildOrderCancelledBody(ctx)
            );

            // ─────────────────────────────
            // SECURITY / ACCOUNT
            // ─────────────────────────────
            case LOGIN_NEW_DEVICE -> new NotificationMessage(
                "New login detected",
                "We noticed a new login to your Laundry Heroes account from "
                    + v(ctx, "device")
                    + (isPresent(ctx, "ip") ? " (IP: " + v(ctx, "ip") + ")" : "")
                    + ". If this wasn’t you, please reset your password immediately."
            );

            case PASSWORD_CHANGED -> new NotificationMessage(
                "Password changed",
                "Your Laundry Heroes account password was changed successfully. "
                    + "If you didn’t make this change, contact support immediately."
            );

            case PROFILE_ACTIVATED -> new NotificationMessage(
                "Profile activated",
                "Your Laundry Heroes account (" + vOrDefault(ctx, "email", "your email")
                    + ") has been successfully created and activated. You can now place orders and enjoy full access to the app."
            );

            // ─────────────────────────────
            // ADDRESS
            // ─────────────────────────────
            case ADDRESS_ADDED -> new NotificationMessage(
                "New address added",
                "You added a new address"
                    + (isPresent(ctx, "addressLabel") ? " \"" + v(ctx, "addressLabel") + "\"" : "")
                    + (isPresent(ctx, "city") ? " in " + v(ctx, "city") : "")
                    + " to your Laundry Heroes profile."
            );

            // ─────────────────────────────
            // ADMIN / MARKETING
            // ─────────────────────────────
            case ADMIN_BROADCAST -> new NotificationMessage(
                vOrDefault(ctx, "title", "Laundry Heroes update"),
                vOrDefault(ctx, "body",
                    "There’s a new update from Laundry Heroes. Open the app to see more.")
            );
        };
    }

    // ─────────────────────────────
    // Helpers
    // ─────────────────────────────

    private String v(Map<String, Object> ctx, String key) {
        Object o = ctx.get(key);
        return (o == null) ? "" : String.valueOf(o);
    }

    private boolean isPresent(Map<String, Object> ctx, String key) {
        Object o = ctx.get(key);
        if (o == null) return false;
        String s = String.valueOf(o).trim();
        return !s.isEmpty();
    }

    private String vOrDefault(Map<String, Object> ctx, String key, String fallback) {
        String val = v(ctx, key).trim();
        return val.isEmpty() ? fallback : val;
    }

    private String buildOrderCancelledBody(Map<String, Object> ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your order #").append(v(ctx, "orderId")).append(" has been cancelled.");
        if (isPresent(ctx, "reason")) {
            sb.append(" Reason: ").append(v(ctx, "reason"));
        }
        return sb.toString();
    }
}
