package com.laundryheroes.core.address;

import com.laundryheroes.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);
}
