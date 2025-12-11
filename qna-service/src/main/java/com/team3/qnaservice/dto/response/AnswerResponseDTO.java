package com.team3.qnaservice.dto.response;

import com.team3.qnaservice.domain.Answer;

public record AnswerResponseDTO(
        Long answerId,
        Long userId,
        Long questionId,
        String answerContent
) {
    public static AnswerResponseDTO fromEntity(Answer answer) {
        return  new AnswerResponseDTO(
                answer.getAnswerId(),
                answer.getUserId(),
                answer.getQuestionId(),
                answer.getAnswerContent()
        );
    }
}