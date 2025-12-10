package com.team3.qnaservice.service;

import com.team3.qnaservice.domain.Answer;
import com.team3.qnaservice.domain.Question;
import com.team3.qnaservice.dto.request.AnswerCreateDTO;
import com.team3.qnaservice.dto.request.AnswerUpdateDTO;
import com.team3.qnaservice.dto.response.AnswerResponseDTO;
import com.team3.qnaservice.repository.AnswerRepository;
import com.team3.qnaservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public AnswerResponseDTO createAnswer(AnswerCreateDTO dto, Long userId, Long questionId, String role) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
        Answer answer = dto.toEntity();
        if(!role.equals("TRAINER")){
            throw new IllegalStateException("트레이너만 답변을 등록할 수 있습니다.");
        }
        answer.assignUserId(userId);
        answer.assignQuestionId(questionId);

        Answer saveAnswer = answerRepository.save(answer);
        question.markAsAnswered();
        return AnswerResponseDTO.fromEntity(saveAnswer);
    }

    public List<AnswerResponseDTO> getAnswersByAnswerContent(String answerContent) {
        return answerRepository.findByAnswerContentContaining(answerContent)
                .stream()
                .map(AnswerResponseDTO::fromEntity)
                .toList();
    }
    public List<AnswerResponseDTO>  getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId)
                .stream()
                .map(AnswerResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void updateAnswer(Long answerId, AnswerUpdateDTO dto, Long userId, Long questionId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));
        if(!answer.getUserId().equals(userId)){
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
        answer.update(dto.answerContent());
    }

    @Transactional
    public void  deleteAnswer(Long answerId, Long userId, Long questionId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));
        if(!answer.getUserId().equals(userId)){
            throw new IllegalStateException("본인이 작성한 답변만 삭제할 수 있습니다.");
        }
        answerRepository.delete(answer);
    }

}
