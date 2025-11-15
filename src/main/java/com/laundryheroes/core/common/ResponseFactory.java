package com.laundryheroes.core.common;

import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public <T> ApiResponse<T> success(ResponseCode code, T data) {
        return new ApiResponse<>(code.code(), code.message(), data);
    }

    public <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message(), data);
    }

    public <T> ApiResponse<T> error(ResponseCode code) {
        return new ApiResponse<>(code.code(), code.message(), null);
    }

    public <T> ApiResponse<T> error(ResponseCode code, String message) {
        return new ApiResponse<>(code.code(), message, null);
    }
}
