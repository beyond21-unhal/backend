package com.team3.notificationservice.dto.request;

import lombok.Builder;

public record AnswerCreatedRequest(
        Long questionId,
        Long answerId,
        Long questionOwnerId,
        Long trainerId, // QaA 게시판은 트레이너만 답변하니 알람은 trainerId로
        String questionTitle
) {
    @Builder
    public AnswerCreatedRequest { }
}
