package com.laundryheroes.core.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.address.AddressRepository;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.servicecatalog.LaundryServiceRepository;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final LaundryServiceRepository laundryServiceRepository;
    private final ResponseFactory responseFactory;

    @Transactional
    public ApiResponse<OrderResponse> createOrder(User user, CreateOrderRequest request) {

        Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
                .orElse(null);

        if (address == null) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setPickupTimeRequested(request.getPickupTimeRequested());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest itemReq : request.getItems()) {
            LaundryService service = laundryServiceRepository.findById(itemReq.getServiceId())
                    .orElse(null);

            if (service == null) {
                return responseFactory.error(ResponseCode.SERVICE_NOT_FOUND);
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setLaundryService(service);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(service.getPrice());

            double subtotal = service.getPrice() * itemReq.getQuantity();
            item.setSubtotal(subtotal);

            total += subtotal;
            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        orderRepository.save(order);

        return responseFactory.success(ResponseCode.ORDER_SUCCESS, toResponse(order));
    }

    @Transactional
    public ApiResponse<OrderResponse> acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(order));
    }

    public ApiResponse<List<OrderResponse>> userOrders(User user) {
        List<OrderResponse> list = orderRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }

    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(i -> new OrderItemResponse(
                        i.getLaundryService().getId(),
                        i.getLaundryService().getServiceType(),
                        i.getLaundryService().getItemType(),
                        i.getLaundryService().getCategory(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getAddress() != null ? order.getAddress().getId() : null,
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getPickupTimeRequested(),
                items
        );
    }
}
