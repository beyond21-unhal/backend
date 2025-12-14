package com.team3.qnaservice.service;

import com.team3.qnaservice.domain.Question;
import com.team3.qnaservice.dto.request.QuestionCreateDTO;
import com.team3.qnaservice.dto.request.QuestionUpdateDTO;
import com.team3.qnaservice.dto.response.QuestionResponseDTO;
import com.team3.qnaservice.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 생성 성공")
    void createQuestion_success() {
        // given
        QuestionCreateDTO dto = new QuestionCreateDTO("제목", "내용");

        Question question = dto.toEntity();
        question.assignUserId(1L);

        Question saved = Question.builder()
                .questionTitle("제목")
                .questionContent("내용")
                .build();
        saved.assignUserId(1L);

        when(questionRepository.save(any())).thenReturn(saved);

        // when
        QuestionResponseDTO result = questionService.createQuestion(dto, 1L);

        // then
        assertThat(result.questionTitle()).isEqualTo("제목");
        assertThat(result.userId()).isEqualTo(1L);
        verify(questionRepository, times(1)).save(any());
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("키워드 검색 성공")
    void searchQuestions_success() {
        Question q1 = Question.builder().questionTitle("운동 질문").questionContent("내용1").build();
        Question q2 = Question.builder().questionTitle("헬스 질문").questionContent("내용2").build();

        when(questionRepository
                .findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase("문", "문"))
                .thenReturn(List.of(q1, q2));

        List<QuestionResponseDTO> results = questionService.searchQuestions("문");

        assertThat(results).hasSize(2);
        verify(questionRepository, times(1))
                .findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase("문", "문");
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("내 질문 조회 성공")
    void getMyQuestions_success() {
        Question q1 = Question.builder().questionTitle("제목1").questionContent("a").build();
        q1.assignUserId(1L);

        Question q2 = Question.builder().questionTitle("제목2").questionContent("b").build();
        q2.assignUserId(1L);

        when(questionRepository.findQuestionsByUserId(1L)).thenReturn(List.of(q1, q2));

        List<QuestionResponseDTO> results = questionService.getMyQuestions(1L);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).userId()).isEqualTo(1L);
        verify(questionRepository).findQuestionsByUserId(1L);
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 수정 성공")
    void updateQuestion_success() {
        Question q = Question.builder()
                .questionTitle("old title")
                .questionContent("old content")
                .build();
        q.assignUserId(1L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        QuestionUpdateDTO dto = new QuestionUpdateDTO("new title", "new content");

        QuestionResponseDTO result = questionService.updateQuestion(1L, dto, 1L);

        assertThat(result.questionTitle()).isEqualTo("new title");
        assertThat(result.questionContent()).isEqualTo("new content");
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 수정 실패 - 본인이 아님")
    void updateQuestion_wrongUser_fail() {
        Question q = Question.builder()
                .questionTitle("old")
                .questionContent("old")
                .build();
        q.assignUserId(1L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        QuestionUpdateDTO dto = new QuestionUpdateDTO("new", "new");

        assertThatThrownBy(() -> questionService.updateQuestion(1L, dto, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수정 권한이 없습니다.");
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 수정 실패 - 이미 답변 있음")
    void updateQuestion_answered_fail() {
        Question q = Question.builder()
                .questionTitle("old")
                .questionContent("old")
                .build();
        q.assignUserId(1L);
        q.markAsAnswered(); // 질문 상태: answered

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        QuestionUpdateDTO dto = new QuestionUpdateDTO("new", "new");

        assertThatThrownBy(() -> questionService.updateQuestion(1L, dto, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("답변이 존재하는 질문은 수정할 수 없습니다.");
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 삭제 성공")
    void deleteQuestion_success() {
        Question q = Question.builder()
                .questionTitle("제목")
                .questionContent("내용")
                .build();
        q.assignUserId(1L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        questionService.deleteQuestion(1L, 1L);

        verify(questionRepository, times(1)).delete(q);
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 삭제 실패 - 본인이 아님")
    void deleteQuestion_wrongUser_fail() {
        Question q = Question.builder().questionTitle("a").questionContent("b").build();
        q.assignUserId(1L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        assertThatThrownBy(() -> questionService.deleteQuestion(1L, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인이 작성한 질문만 삭제할 수 있습니다.");
    }

    // -----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("질문 삭제 실패 - 이미 답변 있음")
    void deleteQuestion_answered_fail() {
        Question q = Question.builder().questionTitle("a").questionContent("b").build();
        q.assignUserId(1L);
        q.markAsAnswered();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));

        assertThatThrownBy(() -> questionService.deleteQuestion(1L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("답변이 존재하는 질문은 삭제할 수 없습니다.");
    }
}
