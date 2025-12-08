package com.laundryheroes.core.admin.dashboard;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class DashboardActivityItem {

    private ZonedDateTime timestamp;
    private String type;      // e.g. "ORDER_STATUS_CHANGED", "NEW_ORDER", "NEW_USER"
    private String message;   // human-readable
}
