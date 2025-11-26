package com.laundryheroes.core.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUser(Long id,User user);
}
