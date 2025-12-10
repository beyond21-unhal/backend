package com.team3.memberservice.dto.response;

import lombok.Builder;

public record TokenResponseDTO(
        String accessToken
) {
    @Builder
    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
