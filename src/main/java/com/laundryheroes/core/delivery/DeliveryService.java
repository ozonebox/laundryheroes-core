package com.laundryheroes.core.delivery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.order.Order;
import com.laundryheroes.core.order.OrderRepository;
import com.laundryheroes.core.order.OrderStatus;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;
import com.laundryheroes.core.user.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRequestRepository deliveryRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ResponseFactory responseFactory;

    @Transactional
    public ApiResponse<DeliveryResponse> createDelivery(Long orderId) {

        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        if (order.getDelivery() != null) {
            return responseFactory.error(ResponseCode.DELIVERY_ALREADY_EXISTS);
        }

        DeliveryRequest d = new DeliveryRequest();
        d.setOrder(order);
        d.setDeliveryAddress(order.getAddress());
        d.setStatus(DeliveryStatus.REQUESTED);

        deliveryRepo.save(d);

        order.setDelivery(d);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        orderRepo.save(order);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> assignDriver(Long deliveryId, Long driverId) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);
        if (d == null) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        if (d.getStatus() != DeliveryStatus.REQUESTED) {
            return responseFactory.error(ResponseCode.INVALID_STATUS);
        }

        User driver = userRepo.findById(driverId).orElse(null);
        if (driver == null || driver.getRole() == UserRole.CUSTOMER) {
            return responseFactory.error(ResponseCode.INVALID_DRIVER);
        }

        d.setAssignedDriver(driver);
        d.setStatus(DeliveryStatus.DRIVER_ASSIGNED);

        deliveryRepo.save(d);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> changeDriver(Long deliveryId, Long newDriverId) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);
        if (d == null) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        User driver = userRepo.findById(newDriverId).orElse(null);
        if (driver == null || driver.getRole() == UserRole.CUSTOMER) {
            return responseFactory.error(ResponseCode.INVALID_DRIVER);
        }
        
        d.setAssignedDriver(driver);
        d.setStatus(DeliveryStatus.DRIVER_ASSIGNED);
        deliveryRepo.save(d);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> unassignDriver(Long deliveryId) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);
        if (d == null) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        d.setAssignedDriver(null);
        d.setStatus(DeliveryStatus.REQUESTED);

        deliveryRepo.save(d);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> driverUpdateStatus(User driver, Long deliveryId, DeliveryStatus status) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);
        if (d == null) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        if (d.getAssignedDriver() == null ||
                !d.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.UNAUTHORIZED);
        }

        d.setStatus(status);

        if (status == DeliveryStatus.DELIVERED) {
            Order order = d.getOrder();
            order.setStatus(OrderStatus.COMPLETED);
        }

        deliveryRepo.save(d);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> adminUpdateStatus(Long deliveryId, DeliveryStatus status) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);
        if (d == null) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        d.setStatus(status);

        if (status == DeliveryStatus.DELIVERED) {
            Order order = d.getOrder();
            order.setStatus(OrderStatus.COMPLETED);
        }

        deliveryRepo.save(d);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(d));
    }

   

    public DeliveryResponse toResponseDirect(DeliveryRequest d) {
         
        return new DeliveryResponse(
                d.getId(),
                d.getOrder().getId(),
                d.getDeliveryAddress().getId(),
                d.getStatus(),
                d.getCreatedAt()
        );
    }
}
