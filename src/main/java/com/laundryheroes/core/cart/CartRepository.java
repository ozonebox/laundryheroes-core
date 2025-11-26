package com.laundryheroes.core.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);


    void deleteByUser(User user);
}
