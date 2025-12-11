package com.team3.dietplanservice.dto.response;

public record ApiResponse<T>(
        String status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "요청 성공", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("FAIL", message, null);
    }
}