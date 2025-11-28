package com.laundryheroes.core.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;

public interface NotificationPreferenceRepository
        extends JpaRepository<NotificationPreference, Long> {

    Optional<NotificationPreference> findByUserAndCategory(
        User user,
        NotificationCategory category
    );

    List<NotificationPreference> findByUser(User user);
}
