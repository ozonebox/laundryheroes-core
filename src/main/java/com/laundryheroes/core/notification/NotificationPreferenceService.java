package com.laundryheroes.core.notification;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository prefRepo;

    @Transactional(readOnly = true)
    public List<NotificationPreferenceDto> getPreferencesForUser(User user) {

        
        return prefRepo.findByUser(user)
                .stream()
                .map(NotificationPreferenceDto::fromEntity)
                .toList();
    }

    @Transactional
    public List<NotificationPreferenceDto> updatePreferences(
        User user,
        List<NotificationPreferenceDto> request
    ) {
        for (NotificationPreferenceDto dto : request) {
            NotificationPreference pref = prefRepo
                .findByUserAndCategory(user, dto.getCategory())
                .orElseGet(() -> {
                    NotificationPreference p = new NotificationPreference();
                    p.setUser(user);
                    p.setCategory(dto.getCategory());
                    return p;
                });

            pref.setPushEnabled(dto.isPushEnabled());
            pref.setEmailEnabled(dto.isEmailEnabled());
            pref.setWhatsappEnabled(dto.isWhatsappEnabled());

            prefRepo.save(pref);
        }

        return getPreferencesForUser(user);
    }
}
