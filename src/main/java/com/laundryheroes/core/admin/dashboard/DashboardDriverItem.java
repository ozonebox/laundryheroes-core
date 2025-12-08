package com.laundryheroes.core.admin.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardDriverItem {

    private Long id;
    private String name;
    private String email;
    private long activePickups;
    private long activeDeliveries;
}
