package com.laundryheroes.core.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse<T> {

    private String responseCode;
    private String responseMessage;
    private T data;

    @JsonIgnore
    private final Map<String, Object> additional = new HashMap<>();

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

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setData(T data) {
        this.data = data;
    }

    // These will be flattened into top-level JSON fields
    @JsonAnyGetter
    public Map<String, Object> getAdditional() {
        return additional;
    }

    public ApiResponse<T> addField(String key, Object value) {
        this.additional.put(key, value);
        return this;
    }
}
