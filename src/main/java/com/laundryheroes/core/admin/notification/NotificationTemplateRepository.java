package com.laundryheroes.core.admin.notification;


import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.notification.NotificationCategory;

import java.util.List;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    List<NotificationTemplate> findByActiveTrue();

    List<NotificationTemplate> findByCategory(NotificationCategory category);

    List<NotificationTemplate> findByActiveTrueAndCategory(NotificationCategory category);
}
