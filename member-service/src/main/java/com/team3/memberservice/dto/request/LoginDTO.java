package com.team3.memberservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO (
        @NotBlank(message = "아이디가 빈 값일수는 없습니다.")
        String id,
        @NotBlank(message = "비밀번호는 빈 값일 수 없습니다.")
        String password
){
}
