package com.team3.workoutplanservice.dto.response;

public record ApiResponse<T>(
        String status,  // "SUCCESS" or "FAIL"
        String message, // "요청 성공" or 에러 메시지
        T data          // 실제 데이터 (MemberDto, PlanDto 등)
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "요청 성공", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("FAIL", message, null);
    }
}