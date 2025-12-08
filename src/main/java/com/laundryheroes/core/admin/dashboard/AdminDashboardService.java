package com.laundryheroes.core.admin.dashboard;

import com.laundryheroes.core.auth.UserService;
import com.laundryheroes.core.delivery.DeliveryRepository;
import com.laundryheroes.core.order.*;
import com.laundryheroes.core.pickup.PickupRepository;
import com.laundryheroes.core.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PickupRepository pickupRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private static final ZoneId ZONE = ZoneId.of("Asia/Qatar");

    public DashboardResponse getDashboard() {

        // ----------------------------
        // Time ranges
        // ----------------------------
        ZonedDateTime zonedNow = ZonedDateTime.now(ZONE);
        ZonedDateTime zonedStartOfDay = zonedNow.toLocalDate().atStartOfDay(ZONE);
        Instant now = zonedNow.toInstant();
        Instant startOfDay=zonedStartOfDay.toInstant();
        // ----------------------------
        // KPIs
        // ----------------------------
        long ordersToday = orderRepository.countByCreatedAtBetween(startOfDay, now);

        BigDecimal revenueToday = orderRepository.revenueForPeriod(startOfDay, now);

        long activeOrders = orderRepository.countByStatusIn(
                List.of(
                        OrderStatus.PENDING,
                        OrderStatus.ACCEPTED,
                        OrderStatus.PICKING_UP,
                        OrderStatus.RECEIVED,
                        OrderStatus.WASHING,
                        OrderStatus.IRONING,
                        OrderStatus.READY,
                        OrderStatus.OUT_FOR_DELIVERY
                )
        );

        long newUsersToday = userRepository.countByCreatedAtBetween(startOfDay, now);

        long activeDrivers = userRepository.countByRole(UserRole.DRIVER);

        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("ordersToday", ordersToday);
        kpis.put("revenueToday", revenueToday);
        kpis.put("activeOrders", activeOrders);
        kpis.put("newUsersToday", newUsersToday);
        kpis.put("activeDrivers", activeDrivers);

        // ----------------------------
        // Order stage counts
        // ----------------------------
        Map<String, Long> orderStageCounts = new LinkedHashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            orderStageCounts.put(status.name(), orderRepository.countByStatus(status));
        }

        // ----------------------------
        // Orders by hour (0–23)
        // ----------------------------
        List<Long> ordersByHour = new ArrayList<>(Collections.nCopies(24, 0L));

        List<Object[]> hourRows = orderRepository.countOrdersByHour(startOfDay, now);
        for (Object[] r : hourRows) {
        Instant ts = (Instant) r[0];      // createdAt
        int hour = ts.atZone(ZONE).getHour();

        long count = ((Number) r[1]).longValue();

        if (hour >= 0 && hour < 24) {
                ordersByHour.set(hour, count);
        }
        }


        // ----------------------------
        // Service type breakdown
        // ----------------------------
        Map<String, Long> serviceBreakdown =
                orderItemRepository.breakdownByServiceType()
                        .stream()
                        .collect(Collectors.toMap(
                                r -> r[0].toString(),
                                r -> ((Number) r[1]).longValue()
                        ));

        // ----------------------------
        // Driver panel
        // ----------------------------
        // You still have findByRole(String) so we use it here:
        List<User> drivers = userRepository.findByRole(UserRole.DRIVER);

        List<Map<String, Object>> driversPanel = drivers.stream()
                .map(d -> {
                    String fullName =
                            ((d.getFirstName() == null ? "" : d.getFirstName()) + " " +
                             (d.getLastName() == null ? "" : d.getLastName())).trim();

                    return Map.<String, Object>of(
                            "id", d.getId(),
                            "name", fullName.isEmpty() ? d.getEmail() : fullName,
                            "activePickups", pickupRepository.countActivePickupsForDriver(d),
                            "activeDeliveries", deliveryRepository.countActiveDeliveriesForDriver(d)
                    );
                })
                .collect(Collectors.toList());

        // ----------------------------
        // Top customers
        // ----------------------------
        List<Map<String, Object>> topCustomers =
        orderRepository.findTopCustomers()
                .stream()
                .map(row -> {
                    Object[] cols = (Object[]) row; // optional if your stream type is just Object

                    Long userId = ((Number) cols[0]).longValue();
                    BigDecimal total = new BigDecimal(cols[1].toString());

                    User u = userService.getUserEntityById(userId);

                    String name = (
                            (u.getFirstName() == null ? "" : u.getFirstName()) + " " +
                            (u.getLastName() == null ? "" : u.getLastName())
                    ).trim();

                    return Map.<String, Object>of(
                            "id", u.getId(),
                            "name", name.isEmpty() ? u.getEmail() : name,
                            "totalSpent", total
                    );
                })
                .collect(Collectors.toList());

        // ----------------------------
        // Latest users
        // ----------------------------
        List<User> latestUsers = userRepository.findTop20ByOrderByCreatedAtDesc();

        List<Map<String, Object>> latestUsersList =
                latestUsers.stream().map(u -> {
                    String fullName =
                            ((u.getFirstName() == null ? "" : u.getFirstName()) +
                             " " +
                             (u.getLastName() == null ? "" : u.getLastName())).trim();

                    return Map.<String, Object>of(
                            "id", u.getId(),
                            "name", fullName.isEmpty() ? u.getEmail() : fullName,
                            "email", u.getEmail(),
                            "createdAt", u.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        // ----------------------------
        // Latest order activity (20)
        // ----------------------------
        List<Order> latestOrders = orderRepository.findTop20ByOrderByCreatedAtDesc();

        List<Map<String, Object>> activity =
                latestOrders.stream().map(o -> Map.<String, Object>of(
                        "orderId", o.getId(),
                        "status", o.getStatus().name(),
                        "customer",
                            o.getUser().getFirstName() != null
                                    ? o.getUser().getFirstName()
                                    : o.getUser().getEmail(),
                        "timestamp", o.getCreatedAt()
                )).collect(Collectors.toList());

        return DashboardResponse.builder()
                .kpis(kpis)
                .orderStageCounts(orderStageCounts)
                .activity(activity)
                .drivers(driversPanel)
                .topCustomers(topCustomers)
                .latestUsers(latestUsersList)
                .ordersByHour(ordersByHour)
                .serviceBreakdown(serviceBreakdown)
                .build();
    }
}
