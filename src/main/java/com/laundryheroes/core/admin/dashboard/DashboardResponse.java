package com.laundryheroes.core.admin.dashboard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Map<String, Object> kpis;

    private Map<String, Long> orderStageCounts;

    private List<Map<String, Object>> activity;

    private List<Map<String, Object>> drivers;

    private List<Map<String, Object>> topCustomers;

    private List<Map<String, Object>> latestUsers;

    private List<Long> ordersByHour; // 24-hour array

    private Map<String, Long> serviceBreakdown;
}
