package com.laundryheroes.core.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.Address;
import com.laundryheroes.core.address.AddressRepository;
import com.laundryheroes.core.address.AddressResponse;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.notification.NotificationCategory;
import com.laundryheroes.core.notification.NotificationPublisher;
import com.laundryheroes.core.notification.NotificationTemplate;
import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.servicecatalog.LaundryServiceRepository;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.websocket.AdminRealtimeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final LaundryServiceRepository laundryServiceRepository;
    private final ResponseFactory responseFactory;
    private final NotificationPublisher notificationPublisher;
    private final AdminRealtimeService realtimeService;

    @Transactional
    public ApiResponse<OrderResponse> createOrder(User user, CreateOrderRequest request) {

        Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
                .orElse(null);
        if (address == null) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }
        Address deliveryAddress = address;
        if(request.getDeliveryAddressId() != request.getAddressId()){
            deliveryAddress = addressRepository.findByIdAndUser(request.getDeliveryAddressId(), user)
                    .orElse(null);
            if (deliveryAddress == null) {
                return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
            }
        }
        
        

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setServiceSpeed(request.getServiceSpeed());
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
        notificationPublisher.notifyUser(
        user,
        NotificationCategory.ORDER_UPDATE,
        NotificationTemplate.ORDER_CREATED,
            Map.of(
                "orderId", order.getId(),
                "total", order.getTotalAmount()
                //"order", order
            )
        );
        realtimeService.push(
            ResponseCode.SUCCESS.code(),
            "Order status updated",
            Map.of(
                "orders", List.of(toResponse(order))
            )
        );

        return responseFactory.success(ResponseCode.ORDER_SUCCESS, toResponse(order));
    }

    

    public ApiResponse<List<OrderResponse>> userOrders(User user) {
        List<OrderResponse> list = orderRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();

        return responseFactory.success(ResponseCode.SUCCESS, list);
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
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(i -> new OrderItemResponse(
                        i.getLaundryService().getId(),
                        i.getLaundryService().getServiceType(),
                        i.getLaundryService().getItemType(),
                        i.getLaundryService().getCategory(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubtotal(),
                        i.getLaundryService().getSpeed()
                ))
                .toList();

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
                items,
                order.getPickup(),
                order.getDelivery()
        );
    }
}
