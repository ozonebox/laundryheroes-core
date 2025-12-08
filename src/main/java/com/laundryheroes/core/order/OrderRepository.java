// src/main/java/com/laundryheroes/core/order/OrderRepository.java
package com.laundryheroes.core.order;

import com.laundryheroes.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Optional<Order> findByIdAndUser(Long id, User user);

    long countByCreatedAtBetween(Instant start, Instant end);
    long countByStatusIn(List<OrderStatus> statuses);
    long countByStatus(OrderStatus status);
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.createdAt BETWEEN :start AND :end
    """)
    BigDecimal revenueForPeriod(Instant start, Instant end);
    
    @Query("""
    SELECT FUNCTION('DATE_TRUNC', 'hour', o.createdAt) AS hour,
           COUNT(o)
        FROM Order o
        WHERE o.createdAt BETWEEN :start AND :end
        GROUP BY FUNCTION('DATE_TRUNC', 'hour', o.createdAt)
        ORDER BY hour
    """)
    List<Object[]> countOrdersByHour(Instant start, Instant end);
    
    @Query(value = """
        SELECT o.user_id AS userId,
            COUNT(*) AS ordersCount
        FROM orders o
        GROUP BY o.user_id
        ORDER BY ordersCount DESC
        LIMIT 20
    """, nativeQuery = true)
    List<Object[]> findTopCustomers();

    List<Order> findTop20ByOrderByCreatedAtDesc();
}
