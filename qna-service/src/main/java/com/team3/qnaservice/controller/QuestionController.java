package com.team3.qnaservice.controller;

import com.team3.qnaservice.dto.request.QuestionCreateDTO;
import com.team3.qnaservice.dto.request.QuestionUpdateDTO;
import com.team3.qnaservice.dto.response.ApiResponse;
import com.team3.qnaservice.dto.response.QuestionResponseDTO;
import com.team3.qnaservice.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "질문 작성 API 입니다.")
    @PostMapping("/")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> createQuestion(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestBody QuestionCreateDTO dto) {

        QuestionResponseDTO response = questionService.createQuestion(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "키워드로 질문 검색 API 입니다.")
    @GetMapping("/search")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> search(
            @RequestParam String keyword) {

        List<QuestionResponseDTO> results = questionService.searchQuestions(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "내 질문 찾기 API 입니다.")
    @GetMapping("/search/myQuestion")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getMyQuestion(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {

        List<QuestionResponseDTO> results = questionService.getMyQuestions(userId);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "questionId로 질문 조회 API 입니다.")
    @GetMapping("/{questionId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getQuestionById(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @PathVariable Long questionId) {

        QuestionResponseDTO result = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }


    @Operation(summary = "미답변 질문 조회 API(트레이너 전용)")
    @GetMapping("/search/unanswered")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getUnansweredQuestions(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {

        List<QuestionResponseDTO> results = questionService.getUnansweredQuestions(role);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "모든 질문 조회 API")
    @GetMapping("/search/all")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getAllQuestions(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role) {

        List<QuestionResponseDTO> results = questionService.getAllQuestions();
        return ResponseEntity.ok(ApiResponse.success(results));
    }



    @Operation(summary = "질문 수정 API 입니다.")
    @PatchMapping("/{questionId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> updateQuestion(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @PathVariable Long questionId,
            @RequestBody QuestionUpdateDTO dto) {

        QuestionResponseDTO response = questionService.updateQuestion(questionId, dto, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "질문 삭제 API입니다.")
    @DeleteMapping("/{questionId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @PathVariable Long questionId) {

        questionService.deleteQuestion(questionId, userId);
        return ResponseEntity.noContent().build();
    }
}
