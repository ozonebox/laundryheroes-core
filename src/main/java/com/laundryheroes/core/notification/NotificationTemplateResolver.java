package com.laundryheroes.core.notification;

import java.util.Map;

public interface NotificationTemplateResolver {

    NotificationMessage resolve(
        NotificationTemplate template,
        Map<String, Object> context
    );
}
