package com.laundryheroes.core.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // ✅ KEEP: existing string-based role finder
    List<User> findByRole(UserRole role);

    long countByCreatedAtBetween(Instant start, Instant end);

long countByRoleAndProfileStatus(UserRole role, ProfileStatus status);



    // ✅ Existing: drivers/admins/superadmin with a status (good for filters, etc.)
    List<User> findByRoleInAndProfileStatus(
            List<UserRole> roles,
            ProfileStatus profileStatus
    );

    // 🔹 NEW: for "activeDrivers" KPI (or generic role counts)
    long countByRole(UserRole role);

    // 🔹 NEW: for dashboard "latestUsers" if you want more than 10
    List<User> findTop20ByOrderByCreatedAtDesc();
}
