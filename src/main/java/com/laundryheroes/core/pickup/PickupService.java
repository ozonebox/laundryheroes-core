package com.laundryheroes.core.pickup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PickupService {

    private final PickupRequestRepository pickupRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ResponseFactory responseFactory;

    @Transactional
    public ApiResponse<PickupResponse> createPickup(Long orderId) {

        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return responseFactory.error(ResponseCode.ORDER_NOT_FOUND);
        }

        if (order.getPickup() != null) {
            return responseFactory.error(ResponseCode.PICKUP_ALREADY_EXISTS);
        }

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            return responseFactory.error(ResponseCode.INVALID_ORDER_STATUS);
        }

        PickupRequest pickup = new PickupRequest();
        pickup.setOrder(order);
        pickup.setPickupAddress(order.getAddress());
        pickup.setScheduledFor(order.getPickupTimeRequested());
        pickup.setStatus(PickupStatus.REQUESTED);

        pickupRepo.save(pickup);

        order.setPickup(pickup);
        order.setStatus(OrderStatus.PICKING_UP);

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> updateStatus(Long pickupId, PickupStatus status) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);
        if (pickup == null) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        pickup.setStatus(status);

        if (status == PickupStatus.DELIVERED_TO_STORE) {
            Order order = pickup.getOrder();
            order.setStatus(OrderStatus.RECEIVED);
        }

        pickupRepo.save(pickup);

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> assignDriver(Long pickupId, Long driverId) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);
        if (pickup == null) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        if (pickup.getStatus() != PickupStatus.REQUESTED) {
            return responseFactory.error(ResponseCode.INVALID_STATUS);
        }

        User driver = userRepo.findById(driverId).orElse(null);
        if (driver == null || driver.getRole() != UserRole.DRIVER) {
            return responseFactory.error(ResponseCode.INVALID_DRIVER);
        }

        pickup.setAssignedDriver(driver);
        pickup.setStatus(PickupStatus.DRIVER_ASSIGNED);
        pickupRepo.save(pickup);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> changeDriver(Long pickupId, Long newDriverId) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);
        if (pickup == null) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        User driver = userRepo.findById(newDriverId).orElse(null);
        if (driver == null || driver.getRole() != UserRole.DRIVER) {
            return responseFactory.error(ResponseCode.INVALID_DRIVER);
        }

        pickup.setAssignedDriver(driver);
        pickupRepo.save(pickup);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> unassignDriver(Long pickupId) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);
        if (pickup == null) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        pickup.setAssignedDriver(null);
        pickup.setStatus(PickupStatus.REQUESTED);
        pickupRepo.save(pickup);

        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(pickup));
    }

    @Transactional
    public ApiResponse<PickupResponse> driverUpdateStatus(User driver, Long pickupId, PickupStatus status) {

        PickupRequest pickup = pickupRepo.findById(pickupId).orElse(null);
        if (pickup == null) {
            return responseFactory.error(ResponseCode.PICKUP_NOT_FOUND);
        }

        if (pickup.getAssignedDriver() == null ||
                !pickup.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.UNAUTHORIZED);
        }

        pickup.setStatus(status);

        if (status == PickupStatus.DELIVERED_TO_STORE) {
            Order order = pickup.getOrder();
            order.setStatus(OrderStatus.RECEIVED);
        }

        pickupRepo.save(pickup);
        return responseFactory.success(ResponseCode.SUCCESS, toResponseDirect(pickup));
    }

    private PickupResponse toResponse(PickupRequest p) {
        return new PickupResponse(
                p.getId(),
                p.getOrder().getId(),
                p.getPickupAddress().getId(),
                p.getStatus(),
                p.getScheduledFor(),
                p.getCreatedAt()
        );
    }

    public PickupResponse toResponseDirect(PickupRequest p) {
        return new PickupResponse(
                p.getId(),
                p.getOrder().getId(),
                p.getPickupAddress().getId(),
                p.getStatus(),
                p.getScheduledFor(),
                p.getCreatedAt()
        );
    }
}
