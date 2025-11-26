package com.laundryheroes.core.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.AddressResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final ResponseFactory responseFactory;

    @Transactional
    public ApiResponse<OrderResponse> updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }

        if (!isValidTransition(order.getStatus(), newStatus)) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(order));
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {

        return switch (current) {
            case RECEIVED -> next == OrderStatus.WASHING;
            case WASHING  -> next == OrderStatus.IRONING;
            case IRONING  -> next == OrderStatus.READY;
            case READY    -> next == OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> next == OrderStatus.COMPLETED;
            default -> false;
        };
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                new AddressResponse(order.getAddress()),
                order.getStatus(),
                order.getTotalAmount(),
                100.00,
                100.00,
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
}
