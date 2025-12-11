package com.team3.qnaservice.service;

import com.team3.qnaservice.domain.Answer;
import com.team3.qnaservice.domain.Question;
import com.team3.qnaservice.dto.request.AnswerCreateDTO;
import com.team3.qnaservice.dto.request.AnswerUpdateDTO;
import com.team3.qnaservice.dto.response.AnswerResponseDTO;
import com.team3.qnaservice.repository.AnswerRepository;
import com.team3.qnaservice.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTests {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerService answerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 생성 성공")
    void createAnswer_success() {
        // given
        AnswerCreateDTO dto = new AnswerCreateDTO("답변 내용");

        Question question = Question.builder()
                .questionTitle("질문1")
                .questionContent("내용1")
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Answer answer = dto.toEntity();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.save(any())).thenReturn(answer);

        // when
        AnswerResponseDTO result = answerService.createAnswer(dto, 10L, 1L, "TRAINER");

        // then
        assertThat(result.answerContent()).isEqualTo("답변 내용");
        assertThat(result.userId()).isEqualTo(10L);
        verify(answerRepository, times(1)).save(any());
        assertThat(question.isAnswered()).isTrue(); // markAsAnswered 작동 확인
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 생성 실패 - 트레이너가 아님")
    void createAnswer_notTrainer_fail() {
        AnswerCreateDTO dto = new AnswerCreateDTO("내용");

        Question question = Question.builder().questionTitle("질문").questionContent("내용").build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> answerService.createAnswer(dto, 10L, 1L, "USER"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("트레이너만 답변을 등록할 수 있습니다.");
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("내용 검색 성공")
    void getAnswersByContent_success() {
        Answer a1 = Answer.builder().answerContent("헬스 질문 답변").build();
        Answer a2 = Answer.builder().answerContent("헬스 팁").build();

        when(answerRepository.findByAnswerContentContaining("헬스"))
                .thenReturn(List.of(a1, a2));

        List<AnswerResponseDTO> results = answerService.getAnswersByAnswerContent("헬스");

        assertThat(results).hasSize(2);
        verify(answerRepository).findByAnswerContentContaining("헬스");
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("questionId로 검색 성공")
    void getAnswersByQuestionId_success() {
        Answer a1 = Answer.builder().answerContent("답변1").build();
        Answer a2 = Answer.builder().answerContent("답변2").build();

        when(answerRepository.findByQuestionId(5L)).thenReturn(List.of(a1, a2));

        List<AnswerResponseDTO> results = answerService.getAnswersByQuestionId(5L);

        assertThat(results).hasSize(2);
        verify(answerRepository).findByQuestionId(5L);
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 수정 성공")
    void updateAnswer_success() {
        Answer answer = Answer.builder().answerContent("old content").build();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        AnswerUpdateDTO dto = new AnswerUpdateDTO("new content");

        AnswerResponseDTO result = answerService.updateAnswer(1L, dto, 10L, 1L);

        assertThat(result.answerContent()).isEqualTo("new content");
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 수정 실패 - 작성자가 아님")
    void updateAnswer_wrongUser_fail() {
        Answer answer = Answer.builder().answerContent("old").build();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        AnswerUpdateDTO dto = new AnswerUpdateDTO("new");

        assertThatThrownBy(() -> answerService.updateAnswer(1L, dto, 99L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인이 작성한 답변만 수정할 수 있습니다.");
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 수정 실패 - questionId 불일치")
    void updateAnswer_wrongQuestion_fail() {
        Answer answer = Answer.builder().answerContent("old").build();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        AnswerUpdateDTO dto = new AnswerUpdateDTO("new");

        assertThatThrownBy(() -> answerService.updateAnswer(1L, dto, 10L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("질문 ID가 일치하지 않습니다.");
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 삭제 성공")
    void deleteAnswer_success() {
        Answer answer = Answer.builder().answerContent("내용").build();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        answerService.deleteAnswer(1L, 10L, 1L);

        verify(answerRepository, times(1)).delete(answer);
    }

    // ------------------------------------------------------------------------------------------
    @Test
    @DisplayName("답변 삭제 실패 - 작성자가 아님")
    void deleteAnswer_wrongUser_fail() {
        Answer answer = Answer.builder().answerContent("내용").build();
        answer.assignUserId(10L);
        answer.assignQuestionId(1L);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        assertThatThrownBy(() -> answerService.deleteAnswer(1L, 99L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인이 작성한 답변만 삭제할 수 있습니다.");
    }
}
