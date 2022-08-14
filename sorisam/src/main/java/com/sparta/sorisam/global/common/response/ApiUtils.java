package com.sparta.sorisam.global.common.response;

public class ApiUtils {
    public static <T> CommonResponse<T> success(int code, T result) {
        return new CommonResponse<>(code, true, result);
    }
}
