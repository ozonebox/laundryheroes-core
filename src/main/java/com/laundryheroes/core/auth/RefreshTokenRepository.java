package com.laundryheroes.core.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserAndToken(User user,String token);

    void deleteAllByUser(User user);
}
