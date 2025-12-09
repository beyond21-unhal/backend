package com.team3.memberservice.dto.request;

import com.team3.memberservice.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupDTO(
        @NotBlank(message = "아이디가 빈 값일수는 없습니다.")
        String id,
        @NotBlank(message = "비밀번호는 빈 값일 수 없습니다.")
        String password,

        @NotBlank(message = "역할이 빌 수는 없습니다.")
        UserRole role
) {

}
