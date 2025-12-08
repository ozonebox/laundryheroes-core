package com.laundryheroes.core.servicecatalog;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;

@Service
public class LaundryServiceService {

    private final LaundryServiceRepository repository;
    private final ResponseFactory responseFactory;

    public LaundryServiceService(LaundryServiceRepository repository,
                                 ResponseFactory responseFactory) {
        this.repository = repository;
        this.responseFactory = responseFactory;
    }

    @Transactional
    public ApiResponse<ServiceResponse> addService(AddServiceRequest request) {

        LaundryService service = new LaundryService();
        service.setServiceType(request.getServiceType());
        service.setItemType(request.getItemType());
        service.setCategory(request.getCategory());
        service.setPrice(request.getPrice());
        service.setSpeed(request.getSpeed());
        service.setActive(true);

        repository.save(service);

        return responseFactory.success(ResponseCode.SERVICE_SUCCESS, toResponse(service));
    }

    @Transactional
    public ApiResponse<ServiceResponse> editService(Long id, EditServiceRequest request) {

        LaundryService service = repository.findById(id).orElse(null);
        if (service == null) {
            return responseFactory.error(ResponseCode.SERVICE_NOT_FOUND);
        }

        if (request.getServiceType() != null) {
            service.setServiceType(request.getServiceType());
        }
        if (request.getItemType() != null) {
            service.setItemType(request.getItemType());
        }
        if (request.getCategory() != null) {
            service.setCategory(request.getCategory());
        }

        if (request.getSpeed() != null) {
            service.setSpeed(request.getSpeed());
        }

        service.setPrice(request.getPrice());

        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }

        repository.save(service);

        return responseFactory.success(ResponseCode.SERVICE_UPDATED_SUCCESS, toResponse(service));
    }

    @Transactional
    public ApiResponse<?> deleteService(Long id) {

        if (!repository.existsById(id)) {
            return responseFactory.error(ResponseCode.SERVICE_NOT_FOUND);
        }

        repository.deleteById(id);
        return responseFactory.success(ResponseCode.SERVICE_DELETE_SUCCESS, null);
    }

    public ApiResponse<List<ServiceResponse>> getActiveServices() {

        List<ServiceResponse> list = repository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        HashMap<String, Object> charges = new HashMap<>();
        charges.put("serviceFee", 0.00);
        charges.put("deliveryFee", 0.00);
        return responseFactory.success(ResponseCode.SUCCESS, list)
        .addField("charges", responseFactory.success(ResponseCode.SUCCESS, charges));
    }

    

    public ApiResponse<List<ServiceResponse>> getAllServices() {

        List<ServiceResponse> list = repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }

    private ServiceResponse toResponse(LaundryService service) {
        return new ServiceResponse(
                service.getId(),
                service.getServiceType(),
                service.getItemType(),
                service.getSpeed(),
                service.getCategory(),
                service.getPrice(),
                service.isActive()
        );
    }
}
