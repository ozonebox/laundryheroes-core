package com.laundryheroes.core.websocket;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealtimePayload {

    private String responseCode;
    private String responseMessage;

    /**
     * Example:
     * {
     *   "orders": [...],
     *   "users": [...],
     *   "stats": {...}
     * }
     */
    private Map<String, Object> data;
}
