package com.team3.qnaservice.dto.response;

import com.team3.qnaservice.domain.Question;

public record QuestionResponseDTO(
        Long questionId,
        Long userId,
        String questionTitle,
        String questionContent,
        boolean isAnswered
) {
    public static QuestionResponseDTO fromEntity(Question question) {
        return new QuestionResponseDTO(
                question.getQuestionId(),
                question.getUserId(),
                question.getQuestionTitle(),
                question.getQuestionContent(),
                question.isAnswered()
        );
    }
}
