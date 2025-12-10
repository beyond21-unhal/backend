package com.team3.qnaservice.controller;

import com.team3.qnaservice.dto.request.AnswerCreateDTO;
import com.team3.qnaservice.dto.request.AnswerUpdateDTO;
import com.team3.qnaservice.dto.response.AnswerResponseDTO;
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
    public ResponseEntity<AnswerResponseDTO> createAnswer(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestParam Long questionId,
            @RequestBody AnswerCreateDTO dto) {
        AnswerResponseDTO response = answerService.createAnswer(dto, userId, questionId,role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "내용으로 답변 검색 API 입니다.")
    @GetMapping("/search/content")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<AnswerResponseDTO>> searchByContent(@RequestParam String content) {
        return ResponseEntity.ok(answerService.getAnswersByAnswerContent(content));
    }

    @Operation(summary = "questionId로 답변 검색 API 입니다.")
    @GetMapping("/search/questionId")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<AnswerResponseDTO>> searchByQuestionId(
            @RequestParam Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }

    @Operation(summary = "답변 수정 API 입니다.")
    @PatchMapping("/{answerId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(hidden = true) @RequestHeader("X-User-Role") String role,
            @RequestParam Long questionId,
            @PathVariable Long answerId,
            @RequestBody AnswerUpdateDTO dto) {

        AnswerResponseDTO updatedAnswer = answerService.updateAnswer(answerId, dto, userId, questionId);
        return ResponseEntity.ok(updatedAnswer);
    }

    @Operation(summary = "답변 삭제 API 입니다.")
    @DeleteMapping("{answerId}")
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
