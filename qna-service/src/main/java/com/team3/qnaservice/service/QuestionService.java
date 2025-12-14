package com.team3.qnaservice.service;

import com.team3.qnaservice.domain.Question;
import com.team3.qnaservice.dto.request.QuestionCreateDTO;
import com.team3.qnaservice.dto.request.QuestionUpdateDTO;
import com.team3.qnaservice.dto.response.QuestionResponseDTO;
import com.team3.qnaservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponseDTO createQuestion(QuestionCreateDTO dto, Long userId) {
        Question question = dto.toEntity();
        question.assignUserId(userId);
        Question saveQuestion = questionRepository.save(question);
        return QuestionResponseDTO.fromEntity(saveQuestion);
    }

    public List<QuestionResponseDTO> searchQuestions(String keyword) {
        return questionRepository
                .findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(QuestionResponseDTO::fromEntity)
                .toList();
    }

    public List<QuestionResponseDTO> getUnansweredQuestions(String role) {
        return questionRepository
                .findByIsAnsweredFalse()
                .stream()
                .map(QuestionResponseDTO::fromEntity)
                .toList();
    }

    public List<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(QuestionResponseDTO::fromEntity)
                .toList();
    }

    public List<QuestionResponseDTO> getMyQuestions(Long userId) {
        List<Question> questions = questionRepository.findQuestionsByUserId(userId);
        return questions.stream()
                .map(QuestionResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponseDTO getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .map(QuestionResponseDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
    }


    @Transactional
    public QuestionResponseDTO updateQuestion(Long questionId, QuestionUpdateDTO dto, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
        if(!question.getUserId().equals(userId)){
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
        if (question.isAnswered()) {
            throw new IllegalStateException("답변이 존재하는 질문은 수정할 수 없습니다.");
        }
        question.update(dto.questionTitle(), dto.questionContent());
        return QuestionResponseDTO.fromEntity(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드가 존재하지 않습니다."));
        if(!question.getUserId().equals(userId)){
            throw new IllegalStateException("본인이 작성한 질문만 삭제할 수 있습니다.");
        }
        if (question.isAnswered()) {
            throw new IllegalStateException("답변이 존재하는 질문은 삭제할 수 없습니다.");
        }
        questionRepository.delete(question);
    }


}
