package com.laundryheroes.core.admin.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardKpis {

    private long ordersToday;
    private BigDecimal revenueToday;
    private long activeOrders;
    private long newUsersToday;
    private long activeDrivers;
}
