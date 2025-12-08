package com.laundryheroes.core.admin.dashboard;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminDashboardResponse {

    private DashboardKpis kpis;
    private OrderStageCounts orderStageCounts;

    private List<DashboardActivityItem> activity;
    private List<DashboardDriverItem> drivers;
    private List<DashboardCustomerItem> topCustomers;
    private List<DashboardCustomerItem> latestUsers;

    // exactly 24 values (0–23)
    private List<Integer> ordersByHour;

    // e.g. { "DRY_CLEAN": 40, "WASH_PRESS": 35, "IRONING": 25 }
    private Map<String, Integer> serviceBreakdown;
}
