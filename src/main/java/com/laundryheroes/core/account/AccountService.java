

package com.laundryheroes.core.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.address.AddressService;
import com.laundryheroes.core.auth.RefreshTokenService;
import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.info.SupportService;
import com.laundryheroes.core.notification.NotificationCategory;
import com.laundryheroes.core.notification.NotificationLogResponse;
import com.laundryheroes.core.notification.NotificationLogService;
import com.laundryheroes.core.notification.NotificationPreferenceDto;
import com.laundryheroes.core.notification.NotificationPreferenceService;
import com.laundryheroes.core.notification.NotificationPublisher;
import com.laundryheroes.core.notification.NotificationTemplate;
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
    private final NotificationPublisher notificationPublisher;
    private final RefreshTokenService refreshTokenService;
    private final NotificationPreferenceService prefService;
    private final NotificationLogService notiLogService;

    public AccountService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ResponseFactory responseFactory,
                       LaundryServiceService laundryService,
                       AddressService addressService,
                       OrderService orderService,
                       PresetService presetService,
                       SupportService supportService,
                       NotificationPublisher notificationPublisher,
                       RefreshTokenService refreshTokenService,
                       NotificationPreferenceService prefService,
                       NotificationLogService notiLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.responseFactory = responseFactory;
        this.laundryService = laundryService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.presetService = presetService;
        this.supportService = supportService;
        this.notificationPublisher = notificationPublisher;
        this.refreshTokenService = refreshTokenService;
        this.prefService = prefService;
        this.notiLogService = notiLogService;
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
        List<NotificationPreferenceDto> notificationPref =prefService.getPreferencesForUser(user);
        List<NotificationLogResponse> myPushNotifications = notiLogService.getMyLatestPushNotifications(user);
        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .role(user.getRole())
                .serviceFee(0.00)
                .devliiveryFee(0.00)
                .build();
        return responseFactory.success(ResponseCode.SUCCESS, data)
        .addField("laundryServicesList", laundryServicesList)
        .addField("addressList", addressList)
        .addField("ordersList", orderList)
        .addField("presetList", presetList)
        .addField("supportInfo", supportInfo)
        .addField("notificationPref", responseFactory.success(ResponseCode.SUCCESS, notificationPref))
        .addField("myPushNotifications", responseFactory.success(ResponseCode.SUCCESS, myPushNotifications));
        
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
        refreshTokenService.revokeAllForUser(user);
        notificationPublisher.notifyUser(
            user,
            NotificationCategory.SYSTEM_ALERT,
            NotificationTemplate.PASSWORD_CHANGED,
            Map.of(
                "email", user.getEmail()
            )
        );
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