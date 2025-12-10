package com.team3.notificationservice.dto.request;

import lombok.Builder;

public record AnswerCreatedRequest(
        Long questionId,
        Long answerId,
        Long questionOwnerId,
        Long trainerId,
        String questionTitle
) {
    @Builder
    public AnswerCreatedRequest { }
}
