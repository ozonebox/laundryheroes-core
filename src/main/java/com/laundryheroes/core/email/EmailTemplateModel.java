package com.laundryheroes.core.email;

public record EmailTemplateModel(
    String title,
    String message,
    String actionUrl,      // optional
    String actionText,     // optional
    String secondaryText   // optional
) {}
