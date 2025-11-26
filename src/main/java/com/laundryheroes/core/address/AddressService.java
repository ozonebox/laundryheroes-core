package com.laundryheroes.core.address;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final ResponseFactory responseFactory;

    public AddressService(AddressRepository addressRepository, ResponseFactory responseFactory) {
        this.addressRepository = addressRepository;
        this.responseFactory = responseFactory;
    }

    // Add address
    @Transactional
    public ApiResponse<AddressResponse> addAddress(User user, AddAddressRequest request) {

        boolean isFirst = addressRepository.findByUser(user).isEmpty();

        Address address = new Address();
        address.setUser(user);
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setAddress(request.getAddress());
        address.setDefault(isFirst);
        address.setLat(request.getLat());
        address.setLong(request.getLng());
        address.setLabel(request.getLabel());

        Address saved = addressRepository.save(address);

        return responseFactory.success(ResponseCode.ADDRESS_SUCCESS,
                new AddressResponse(saved.getId(), saved.getName(), saved.getPhone(), saved.getAddress(), saved.isDefault(),saved.getLat(),saved.getLng(),saved.getLabel(),saved.getStatus())
        );
    }

    // Edit
    @Transactional
    public ApiResponse<AddressResponse> editAddress(User user, EditAddressRequest request) {

        Address address = addressRepository.findById(request.getId())
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElse(null);

        if (address == null) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }

        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setAddress(request.getAddress());
        address.setLat(request.getLat());
        address.setLong(request.getLng());
        address.setLabel(request.getLabel());

        Address saved = addressRepository.save(address);

        return responseFactory.success(ResponseCode.ADDRESS_EDIT_SUCCESS,
                new AddressResponse(saved.getId(), saved.getName(), saved.getPhone(), saved.getAddress(), saved.isDefault(),saved.getLat(),saved.getLng(),saved.getLabel(),saved.getStatus())
        );
    }

    // Delete
    @Transactional
    public ApiResponse<?> deleteAddress(User user, Long id) {

        if (!addressRepository.existsByIdAndUser(id, user)) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }

        Address address=addressRepository.findByIdAndUser(id,user)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElse(null);

        if (address == null) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }
        address.setStatus("DELETED");
        addressRepository.save(address);

        return responseFactory.success(ResponseCode.ADDRESS_DELETE_SUCCESS, null);
    }

    // Set Default
    @Transactional
    public ApiResponse<?> setDefault(User user, Long id) {

        Address selected = addressRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElse(null);

        if (selected == null) {
            return responseFactory.error(ResponseCode.ADDRESS_NOT_FOUND);
        }

        List<Address> addresses = addressRepository.findByUser(user);

        for (Address a : addresses) {
            a.setDefault(a.getId().equals(id));
        }

        addressRepository.saveAll(addresses);

        return responseFactory.success(ResponseCode.ADDRESS_DEFAULT_SUCCESS, null);
    }

    // List
    public ApiResponse<List<AddressResponse>> list(User user) {

        List<AddressResponse> items = addressRepository.findByUserAndStatus(user, "ACTIVE")
                .stream()
                .map(a -> new AddressResponse(
                        a.getId(),
                        a.getName(),
                        a.getPhone(),
                        a.getAddress(),
                        a.isDefault(),
                        a.getLat(),
                        a.getLng(),
                        a.getLabel(),
                        a.getStatus()
                ))
                .toList();

        return responseFactory.success(ResponseCode.SUCCESS, items);
    }
}
