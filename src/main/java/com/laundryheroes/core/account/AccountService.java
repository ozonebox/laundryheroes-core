

package com.laundryheroes.core.account;

import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.auth.UserResponse;
import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.user.ProfileStatus;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseFactory responseFactory;

    public AccountService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ResponseFactory responseFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.responseFactory = responseFactory;
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
        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .build();

        return responseFactory.success(ResponseCode.SUCCESS, data);
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