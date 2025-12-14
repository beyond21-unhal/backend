package com.team3.qnaservice.dto.request;

public record AnswerCreatedRequest(
        Long questionOwnerId,
        String questionTitle,
        Long trainerId
) {}
