package com.laundryheroes.core.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundryheroes.core.user.User;
import com.laundryheroes.core.servicecatalog.LaundryService;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndService(User user, LaundryService service);

    void deleteByUser(User user);
}
