package com.laundryheroes.core.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.AddressResponse;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.notification.NotificationCategory;
import com.laundryheroes.core.notification.NotificationPublisher;
import com.laundryheroes.core.notification.NotificationTemplate;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.websocket.AdminRealtimeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final ResponseFactory responseFactory;
    private final NotificationPublisher notificationPublisher;
    private final AdminRealtimeService realtimeService;

    @Transactional
    public ApiResponse<OrderResponse> updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }
        OrderStatus oldStatus = order.getStatus();
        if (!isValidTransition(oldStatus, newStatus)) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
         triggerStatusNotification(order, oldStatus, newStatus);
         realtimeService.push(
            ResponseCode.SUCCESS.code(),
            "Order status updated",
            Map.of(
                "orders", List.of(toResponse(order))
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(order));
    }

     public ApiResponse<List<OrderResponse>> allOrders() {
        List<OrderResponse> list = orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {

        return switch (current) {
            case PENDING -> next == OrderStatus.ACCEPTED;
            case ACCEPTED -> next == OrderStatus.PICKING_UP;
            case PICKING_UP -> next == OrderStatus.RECEIVED;
            case RECEIVED -> next == OrderStatus.WASHING || next == OrderStatus.IRONING|| next == OrderStatus.READY;
            case WASHING  -> next == OrderStatus.IRONING || next == OrderStatus.READY;
            case IRONING  -> next == OrderStatus.READY;
            case READY    -> next == OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> next == OrderStatus.COMPLETED;
            default -> false;
        };
    }

   

    @Transactional
    public ApiResponse<OrderResponse> cancelOrder(User user,Long orderId) {
        // Find order
        Order order = orderRepository.findByIdAndUser(orderId,user).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }

        // Only allow cancel when still pending
        if (order.getStatus() != OrderStatus.PENDING) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        // Set status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
            notificationPublisher.notifyUser(
            order.getUser(),
            NotificationCategory.ORDER_UPDATE,
            NotificationTemplate.ORDER_CANCELLED,
            Map.of("orderId", order.getId())
        );
        realtimeService.push(
            ResponseCode.SUCCESS.code(),
            "Order status updated",
            Map.of(
                "orders", List.of(toResponse(order))
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(order));
    }

    @Transactional
    public ApiResponse<OrderResponse> adminCancelOrder(User user,Long orderId) {
        // Find order
        Order order = orderRepository.findByIdAndUser(orderId,user).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }


        // Set status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        notificationPublisher.notifyUser(
            order.getUser(),
            NotificationCategory.ORDER_UPDATE,
            NotificationTemplate.ORDER_CANCELLED,
            Map.of(
                "orderId", order.getId(),
                "reason", "Cancelled by admin"
            )
        );
        realtimeService.push(
            ResponseCode.SUCCESS.code(),
            "Order status updated",
            Map.of(
                "orders", List.of(toResponse(order))
            )
        );

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(order));
    }


    private OrderResponse toResponse(Order order) {
         UserResponse data = UserResponse.builder()
            .id(order.getUser().getId())
            .email(order.getUser().getEmail())
            .firstName(order.getUser().getFirstName())
            .lastName(order.getUser().getLastName())
            .gender(order.getUser().getGender())
            .profileStatus(order.getUser().getProfileStatus())
            .build();
        return new OrderResponse(
                order.getId(),
                data,
                new AddressResponse(order.getAddress()),
                new AddressResponse(order.getDeliveryAddress()),
                order.getStatus(),
                order.getTotalAmount(),
                0.00,
                0.00,
                order.getPaymentMethod(),
                order.getServiceSpeed(),
                order.getCreatedAt(),
                order.getPickupTimeRequested(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getLaundryService().getId(),
                                i.getLaundryService().getServiceType(),
                                i.getLaundryService().getItemType(),
                                i.getLaundryService().getCategory(),
                                i.getUnitPrice(),
                                i.getQuantity(),
                                i.getSubtotal()
                        )).toList(),
                order.getPickup(),
                order.getDelivery()
        );
        }
    
    private void triggerStatusNotification(
            Order order,
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {
        NotificationTemplate template = switch (newStatus) {
            case ACCEPTED -> NotificationTemplate.ORDER_ACCEPTED;
            case PICKING_UP -> NotificationTemplate.ORDER_PICKUP_STARTED;
            case RECEIVED -> NotificationTemplate.ORDER_RECEIVED;
            case WASHING, IRONING -> NotificationTemplate.ORDER_IN_PROGRESS;
            case READY -> NotificationTemplate.ORDER_READY;
            case OUT_FOR_DELIVERY -> NotificationTemplate.ORDER_OUT_FOR_DELIVERY;
            case COMPLETED -> NotificationTemplate.ORDER_COMPLETED;
            default -> null;
        };

        if (template == null) return;

        notificationPublisher.notifyUser(
            order.getUser(),
            NotificationCategory.ORDER_UPDATE,
            template,
            Map.of(
                "orderId", order.getId(),
                "status", newStatus.name()
            )
        );
    }

    
}
