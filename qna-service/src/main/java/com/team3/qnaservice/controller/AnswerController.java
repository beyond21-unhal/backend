package com.team3.qnaservice.controller;

import com.team3.qnaservice.dto.request.AnswerCreateDTO;
import com.team3.qnaservice.dto.request.AnswerUpdateDTO;
import com.team3.qnaservice.dto.response.AnswerResponseDTO;
import com.team3.qnaservice.dto.response.ApiResponse;
import com.team3.qnaservice.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @Operation(summary = "답변 작성 API 입니다.")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<AnswerResponseDTO>> createAnswer(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestParam Long questionId,
            @RequestBody AnswerCreateDTO dto) {

        AnswerResponseDTO result = answerService.createAnswer(dto, userId, questionId, role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result));
    }

    @Operation(summary = "내용으로 답변 검색 API 입니다.")
    @GetMapping("/search/content")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<AnswerResponseDTO>>> searchByContent(@RequestParam String content) {
        List<AnswerResponseDTO> results = answerService.getAnswersByAnswerContent(content);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "questionId로 답변 검색 API 입니다.")
    @GetMapping("/search/questionId")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<List<AnswerResponseDTO>>> searchByQuestionId(@RequestParam Long questionId) {
        List<AnswerResponseDTO> results = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "답변 수정 API 입니다.")
    @PatchMapping("/{answerId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ApiResponse<AnswerResponseDTO>> updateAnswer(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestParam Long questionId,
            @PathVariable Long answerId,
            @RequestBody AnswerUpdateDTO dto) {
        AnswerResponseDTO updated = answerService.updateAnswer(answerId, dto, userId, questionId);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @Operation(summary = "답변 삭제 API 입니다.")
    @DeleteMapping("/{answerId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deleteAnswer(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestParam Long questionId,
            @PathVariable Long answerId) {
        answerService.deleteAnswer(answerId, userId, questionId);
        return ResponseEntity.noContent().build();
    }
}