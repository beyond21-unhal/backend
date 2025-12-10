package com.team3.memberservice.dto.response;

import lombok.Builder;

public record UserInfoDTO(
        Long userId,
        String username
) {
    @Builder
    public UserInfoDTO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
