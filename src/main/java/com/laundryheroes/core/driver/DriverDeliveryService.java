package com.laundryheroes.core.driver;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.delivery.*;
import com.laundryheroes.core.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverDeliveryService {

    private final DeliveryRequestRepository deliveryRepo;
    private final DeliveryService deliveryService;
    private final ResponseFactory responseFactory;

    public ApiResponse<List<DeliveryResponse>> getAssignedDeliveries(User driver) {

        List<DeliveryResponse> deliveries = deliveryRepo.findAll()
                .stream()
                .filter(d -> d.getAssignedDriver() != null && d.getAssignedDriver().getId().equals(driver.getId()))
                .map(d -> new DeliveryResponse(
                        d.getId(),
                        d.getOrder().getId(),
                        d.getDeliveryAddress().getId(),
                        d.getStatus(),
                        d.getCreatedAt()
                )).toList();

        return responseFactory.success(ResponseCode.SUCCESS, deliveries);
    }

    public ApiResponse<DeliveryResponse> getDeliveryDetails(User driver, Long deliveryId) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);

        if (d == null || d.getAssignedDriver() == null || !d.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        return responseFactory.success(ResponseCode.SUCCESS, deliveryService.toResponseDirect(d));
    }

    @Transactional
    public ApiResponse<DeliveryResponse> updateDeliveryStatus(User driver, Long deliveryId, String newStatus) {

        DeliveryRequest d = deliveryRepo.findById(deliveryId).orElse(null);

        if (d == null || d.getAssignedDriver() == null || !d.getAssignedDriver().getId().equals(driver.getId())) {
            return responseFactory.error(ResponseCode.DELIVERY_NOT_FOUND);
        }

        DeliveryStatus status = DeliveryStatus.valueOf(newStatus.toUpperCase());

        return deliveryService.driverUpdateStatus(driver, deliveryId, status);
    }
}
