package com.laundryheroes.core.common;

public class ApiResponse<T> {

    private String responseCode;
    private String responseMessage;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String responseCode, String responseMessage, T data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public T getData() {
        return data;
    }
}
