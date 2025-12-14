package com.team3.memberservice.dto.response;

import lombok.Builder;

public record UserInfoDTO(
        Long userId,
        String username,
        String role
) {
    @Builder
    public UserInfoDTO(Long userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
