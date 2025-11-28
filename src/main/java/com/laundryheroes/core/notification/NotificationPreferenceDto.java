package com.laundryheroes.core.notification;

import lombok.Data;

@Data
public class NotificationPreferenceDto {

    private NotificationCategory category;
    private boolean pushEnabled;
    private boolean emailEnabled;
    private boolean whatsappEnabled;

    public static NotificationPreferenceDto fromEntity(NotificationPreference p) {
        NotificationPreferenceDto dto = new NotificationPreferenceDto();
        dto.setCategory(p.getCategory());
        dto.setPushEnabled(p.isPushEnabled());
        dto.setEmailEnabled(p.isEmailEnabled());
        dto.setWhatsappEnabled(p.isWhatsappEnabled());
        return dto;
    }
}
