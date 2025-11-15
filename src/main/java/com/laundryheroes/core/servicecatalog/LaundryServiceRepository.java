package com.laundryheroes.core.servicecatalog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaundryServiceRepository extends JpaRepository<LaundryService, Long> {

    List<LaundryService> findByActiveTrue();
}
