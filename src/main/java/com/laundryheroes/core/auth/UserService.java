package com.laundryheroes.core.auth;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.email.EmailService;
import com.laundryheroes.core.user.ProfileStatus;
import com.laundryheroes.core.user.User;
import com.laundryheroes.core.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseFactory responseFactory;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ResponseFactory responseFactory,EmailService emailService,JwtService jwtService,RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.responseFactory = responseFactory;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return responseFactory.error(ResponseCode.DUPLICATE);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), hashedPassword);
        User saved = userRepository.save(user);
        UserResponse data = UserResponse.builder()
            .id(saved.getId())
            .email(saved.getEmail())
            .firstName(saved.getFirstName())
            .lastName(saved.getLastName())
            .gender(saved.getGender())
            .profileStatus(saved.getProfileStatus())
            .build();
        

        return responseFactory.success(ResponseCode.REGISTER_SUCCESS, data);
    }

    @Transactional
    public ApiResponse<UserResponse> login(LoginRequest request) {

        Optional<User> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.LOGIN_FAILED);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.BLOCKED) {
            return responseFactory.error(ResponseCode.USER_BLOCKED);
        }

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);
            user.setLastFailedAt(Instant.now());

            if (attempts >= 5) {
                user.setProfileStatus(ProfileStatus.BLOCKED);
                userRepository.save(user);
                return responseFactory.error(ResponseCode.USER_BLOCKED);
            }

            userRepository.save(user);
            return responseFactory.error(ResponseCode.LOGIN_FAILED);
        }

        user.setFailedAttempts(0);
        user.setLastFailedAt(null);

        if (user.getProfileStatus() == ProfileStatus.PENDING) {
            String accessToken = jwtService.generateAccessTokenPending(user);
             UserResponse data = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .gender(user.getGender())
            .profileStatus(user.getProfileStatus())
            .token(accessToken)
            .build();
            
            return responseFactory.success(ResponseCode.PROFILE_PENDING,data);
        }

        userRepository.save(user);
        String accessToken = jwtService.generateAccessTokenFull(user);
        RefreshToken refreshToken = refreshTokenService.create(user);
        UserResponse data = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .gender(user.getGender())
            .profileStatus(user.getProfileStatus())
            .token(accessToken)
            .refreshToken(refreshToken.getToken())
            .build();
        

        return responseFactory.success(ResponseCode.LOGIN_SUCCESS, data);
    }

    @Transactional
    public ApiResponse<VerifyEmailResponse> sendVerificationOtp(User authUser,VerifyEmailRequest request) {

    Optional<User> optional = userRepository.findByEmail(authUser.getEmail());
    if (optional.isEmpty()) {
        return responseFactory.error(ResponseCode.USER_NOT_FOUND);
    }

    User user = optional.get();

    if (user.getProfileStatus() != ProfileStatus.PENDING) {
        return responseFactory.error(ResponseCode.PROFILE_NOT_PENDING);
    }

    Instant now = Instant.now();

    if (user.getLastOtpSentAt() != null && user.getLastOtpSentAt().plusSeconds(300).isAfter(now)) {
        return responseFactory.error(ResponseCode.OTP_RATE_LIMIT);
    }

    String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
    String authKey = UUID.randomUUID().toString();

    user.setVerificationOtp(otp);
    user.setOtpExpiresAt(now.plusSeconds(300));
    user.setLastOtpSentAt(now);
    user.setVerificationAuthKey(authKey);

    userRepository.save(user);

    emailService.sendEmail(
            user.getEmail(),
            "Your Laundry Heroes Verification Code",
            "Your OTP is: " + otp
    );

    VerifyEmailResponse data = new VerifyEmailResponse(authKey,user.getEmail());
    return responseFactory.success(ResponseCode.EMAIL_SENT, data);
}

    @Transactional
    public ApiResponse<UserResponse> activateUser(User authUser,ActivateUserRequest request) {

        Optional<User> optional = userRepository.findByEmail(authUser.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.ACTIVE) {
            return responseFactory.error(ResponseCode.PROFILE_ALREADY_ACTIVE);
        }

        if (user.getVerificationOtp() == null || user.getOtpExpiresAt() == null || user.getVerificationAuthKey() == null) {
            return responseFactory.error(ResponseCode.INVALID_OTP);
        }

        Instant now = Instant.now();

        if (user.getOtpExpiresAt().isBefore(now)) {
            return responseFactory.error(ResponseCode.OTP_EXPIRED);
        }

        if (!user.getVerificationOtp().equals(request.getOtp())) {
            return responseFactory.error(ResponseCode.INVALID_OTP);
        }

        if (!user.getVerificationAuthKey().equals(request.getAuthKey())) {
            return responseFactory.error(ResponseCode.INVALID_OTP);
        }

        user.setProfileStatus(ProfileStatus.ACTIVE);
        user.setVerificationOtp(null);
        user.setOtpExpiresAt(null);
        user.setLastOtpSentAt(null);
        user.setVerificationAuthKey(null);

        userRepository.save(user);
         UserResponse data = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .gender(user.getGender())
            .profileStatus(user.getProfileStatus())
            .build();

        return responseFactory.success(ResponseCode.PROFILE_ACTIVATED, data);
    }

    @Transactional
    public ApiResponse<ResetPasswordInitResponse> resetPasswordInit(ResetPasswordInitRequest request) {

        Optional<User> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.DISABLED) {
            return responseFactory.error(ResponseCode.USER_DISABLED);
        }

        Instant now = Instant.now();

        if (user.getLastResetOtpSentAt() != null && user.getLastResetOtpSentAt().plusSeconds(300).isAfter(now)) {
            return responseFactory.error(ResponseCode.RESET_OTP_RATE_LIMIT);
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        String authKey = java.util.UUID.randomUUID().toString();

        user.setResetOtp(otp);
        user.setResetOtpExpiresAt(now.plusSeconds(300));
        user.setResetAuthKey(authKey);
        user.setLastResetOtpSentAt(now);

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Laundry Heroes: Reset Your Password",
                "Your password reset OTP is: " + otp
        );

    ResetPasswordInitResponse data = new ResetPasswordInitResponse(authKey,user.getEmail());
    return responseFactory.success(ResponseCode.RESET_OTP_SENT, data);
}

    @Transactional
    public ApiResponse<UserResponse> resetPasswordComplete(ResetPasswordCompleteRequest request) {

        Optional<User> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isEmpty()) {
            return responseFactory.error(ResponseCode.USER_NOT_FOUND);
        }

        User user = optional.get();

        if (user.getProfileStatus() == ProfileStatus.DISABLED) {
            return responseFactory.error(ResponseCode.USER_DISABLED);
        }

        if (user.getResetOtp() == null || user.getResetOtpExpiresAt() == null || user.getResetAuthKey() == null) {
            return responseFactory.error(ResponseCode.INVALID_RESET_OTP);
        }

        Instant now = Instant.now();

        if (user.getResetOtpExpiresAt().isBefore(now)) {
            return responseFactory.error(ResponseCode.RESET_OTP_EXPIRED);
        }

        if (!user.getResetOtp().equals(request.getOtp())) {
            return responseFactory.error(ResponseCode.INVALID_RESET_OTP);
        }

        if (!user.getResetAuthKey().equals(request.getResetAuthKey())) {
            return responseFactory.error(ResponseCode.INVALID_RESET_OTP);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setResetOtp(null);
        user.setResetOtpExpiresAt(null);
        user.setResetAuthKey(null);
        user.setLastResetOtpSentAt(null);

        userRepository.save(user);
        UserResponse data = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .gender(user.getGender())
            .profileStatus(user.getProfileStatus())
            .build();

        return responseFactory.success(ResponseCode.PASSWORD_RESET_SUCCESS, data);
    }

    @Transactional
    public ApiResponse<UserResponse> refresh(String refreshTokenValue) {

        var optionalRotated = refreshTokenService.validateAndRotate(refreshTokenValue);
        if (optionalRotated.isEmpty()) {
            return responseFactory.error(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken newRefresh = optionalRotated.get();
        User user = newRefresh.getUser();

        if (user.getProfileStatus() != ProfileStatus.ACTIVE) {
            return responseFactory.error(ResponseCode.PROFILE_NOT_ACTIVE);
        }

        // Create fresh access token
        String newAccess = jwtService.generateAccessTokenFull(user);

        UserResponse data = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profileStatus(user.getProfileStatus())
                .token(newAccess)
                .refreshToken(newRefresh.getToken())
                .build();

        return responseFactory.success(ResponseCode.SUCCESS, data);
    }
    @Transactional
    public ApiResponse<?> logout(String refreshTokenValue) {

        if (refreshTokenValue != null && !refreshTokenValue.isBlank()) {
            refreshTokenService.revokeAllForToken(refreshTokenValue);
        }

        return responseFactory.success(ResponseCode.LOGOUT_SUCCESS, null);
    }




}
