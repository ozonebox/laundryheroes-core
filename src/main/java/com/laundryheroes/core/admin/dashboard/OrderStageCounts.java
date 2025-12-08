package com.laundryheroes.core.admin.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStageCounts {
    private long PENDING;
    private long ACCEPTED;
    private long PICKING_UP;
    private long RECEIVED;
    private long WASHING;
    private long IRONING;
    private long READY;
    private long OUT_FOR_DELIVERY;
    private long COMPLETED;
    private long CANCELLED;
}
