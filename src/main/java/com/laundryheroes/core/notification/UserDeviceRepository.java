package com.laundryheroes.core.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laundryheroes.core.user.User;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    List<UserDevice> findByUserAndActiveTrue(User user);

    @Modifying
    @Query("update UserDevice d set d.active = false where d.deviceToken = :token")
    void deactivateByToken(@Param("token") String token);
}
