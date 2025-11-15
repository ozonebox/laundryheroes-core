package com.laundryheroes.core.common;

public enum ResponseCode {

    SUCCESS("00", "Success"),
    REGISTER_SUCCESS("00", "Registration successful"),
    LOGIN_FAILED("11", "Invalid credentials"),
    LOGIN_SUCCESS("00", "Login Successful"),
    PROFILE_PENDING("22", "Login Successful. Verify your email"),
    USER_BLOCKED("89", "User account is blocked"),
    USER_DISABLED("88", "User account is disabled"),
    RESET_OTP_SENT("00", "Password reset OTP sent"),
    RESET_OTP_RATE_LIMIT("63", "Wait before requesting another reset OTP"),
    INVALID_RESET_OTP("64", "Invalid reset OTP"),
    RESET_OTP_EXPIRED("65", "Reset OTP expired"),
    PASSWORD_RESET_SUCCESS("00", "Password reset successful"),
    USER_NOT_FOUND("57", "User not found"),
    INVALID_REQUEST("90", "Invalid request"),
    FAILURE("99", "Operation failed"),
    DUPLICATE("92", "Duplicate record"),
    OTP_EXPIRED("58", "OTP expired"),
    INVALID_OTP("59", "Invalid OTP"),
    PROFILE_ALREADY_ACTIVE("60", "Profile already active"),
    PROFILE_ACTIVATED("00", "Profile activated successfully"),
    EMAIL_SENT("00", "OTP sent"),
    OTP_RATE_LIMIT("55", "Wait before requesting another OTP"),
    PROFILE_NOT_PENDING("56", "Profile not in pending state, OTP not allowed"),
    EDIT_PROFILE_SUCCESS("00","Updated successfully"),
    CHANGE_PASSWORD_SUCCESS("00","Password change successful"),
    INVALID_OLD_PASSWORD("11","Invalid current password"),
    ADDRESS_SUCCESS("00","Address added successfully"),
    ADDRESS_EDIT_SUCCESS("00","Address edited successfully"),
    ADDRESS_DELETE_SUCCESS("00","Address deleted successfully"),
    ADDRESS_DEFAULT_SUCCESS("00","Default address set successfully"),
    ADDRESS_NOT_FOUND("11","Invalid Request. Address not found"),
    ORDER_NOT_FOUND("11","Invalid Request. Order not found"),
    ORDER_SUCCESS("00","Order placed successfully"),
    INVALID_ORDER_STATUS("11","Cannot perform action on order"),
    ORDER_UPDATE_SUCCESS("00","Order updated successfully"),
    SERVICE_NOT_FOUND("11","Invalid Request. Service not found"),
    UNAUTHORIZED("909","Invalid Request. You are not authorized"),
    SERVICE_SUCCESS("00","Laundry Service added successfully"),
    SERVICE_DELETE_SUCCESS("00","Laundry Service deleted successfully"),
    SERVICE_UPDATED_SUCCESS("00","Service updated successfully"),
    CART_ITEM_NOT_FOUND("11","Invalid Request. Item not in cart"),
    CART_EMPTY("11","Empty Cart. Please add services to cart"),
    PICKUP_ALREADY_EXISTS("11","Cannot proceed. Pickup already exists."),
    DELIVERY_ALREADY_EXISTS("11","Cannot proceed. Delivery already exists."),
    PICKUP_NOT_FOUND("13","Cannot proceed. Pickup not found."),
    DELIVERY_NOT_FOUND("13","Cannot proceed. Delivery not found."),
    INVALID_STATUS("13","Cannot proceed. Invalid status."),
    INVALID_DRIVER("13","Cannot proceed. Invalid driver."),
    PASSWORD_MISMATCH("12","Passwords don't match");



    

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
