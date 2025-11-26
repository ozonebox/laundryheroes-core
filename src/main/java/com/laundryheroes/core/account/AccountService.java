

package com.laundryheroes.core.account;

import java.util.List;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.AddressService;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.info.SupportMetaResponse;
import com.laundryheroes.core.info.SupportService;
import com.laundryheroes.core.order.OrderService;
import com.laundryheroes.core.servicecatalog.LaundryServiceService;
import com.laundryheroes.core.servicecatalog.PresetService;
import com.laundryheroes.core.servicecatalog.ServiceResponse;
import com.laundryheroes.core.user.ProfileStatus;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseFactory responseFactory;
    private final LaundryServiceService laundryService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final PresetService presetService;
    private final SupportService supportService;

    public AccountService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ResponseFactory responseFactory,
                       LaundryServiceService laundryService,
                       AddressService addressService,
                       OrderService orderService,
                       PresetService presetService,
                       SupportService supportService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.responseFactory = responseFactory;
        this.laundryService = laundryService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.presetService = presetService;
        this.supportService = supportService;
    }

     @Transactional
    public ApiResponse<UserResponse> me(User authUser) {
         if(!authUser.getEmail().equalsIgnoreCase(authUser.getEmail())){
            return responseFactory.error(ResponseCode.INVALID_REQUEST);
        }
        Optional<User> optional = userRepository.findByEmail(authUser.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.BLOCKED ||
            user.getProfileStatus() == ProfileStatus.DISABLED) {
            return responseFactory.error(ResponseCode.USER_BLOCKED);
        }
        ApiResponse<List<ServiceResponse>> laundryServicesList =laundryService.getActiveServices();
        ApiResponse<?> addressList =addressService.list(user);
        ApiResponse<?> orderList =orderService.userOrders(user);
        ApiResponse<?> presetList =presetService.getPresetServices();
        ApiResponse<?> supportInfo =supportService.getSupportInfo();
        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .build();

        return responseFactory.success(ResponseCode.SUCCESS, data)
        .addField("laundryServicesList", laundryServicesList)
        .addField("addressList", addressList)
        .addField("ordersList", orderList)
        .addField("presetList", presetList)
        .addField("supportInfo", supportInfo);
    }


    @Transactional
    public ApiResponse<UserResponse> editProfile(User authUser,EditProfileRequest request) {
         if(!authUser.getEmail().equalsIgnoreCase(request.getEmail())){
            return responseFactory.error(ResponseCode.INVALID_REQUEST);
        }
        Optional<User> optional = userRepository.findByEmail(authUser.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.BLOCKED ||
            user.getProfileStatus() == ProfileStatus.DISABLED) {
            return responseFactory.error(ResponseCode.USER_BLOCKED);
        }

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getGender() != null) user.setGender(request.getGender());

        userRepository.save(user);
        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .build();

        return responseFactory.success(ResponseCode.EDIT_PROFILE_SUCCESS, data);
    }

    @Transactional
    public ApiResponse<UserResponse> changePassword(User authUser,ChangePasswordRequest request) {

        if(!authUser.getEmail().equalsIgnoreCase(request.getEmail())){
            return responseFactory.error(ResponseCode.INVALID_REQUEST);
        }

        Optional<User> optional = userRepository.findByEmail(authUser.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.BLOCKED ||
            user.getProfileStatus() == ProfileStatus.DISABLED) {
            return responseFactory.error(ResponseCode.USER_BLOCKED);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return responseFactory.error(ResponseCode.INVALID_OLD_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return responseFactory.error(ResponseCode.PASSWORD_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFailedAttempts(0);
        user.setLastFailedAt(null);

        userRepository.save(user);

        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .build();

        return responseFactory.success(ResponseCode.CHANGE_PASSWORD_SUCCESS, data);
    }


}