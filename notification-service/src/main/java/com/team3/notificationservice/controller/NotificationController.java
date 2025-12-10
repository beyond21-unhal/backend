package com.team3.notificationservice.controller;

import com.team3.notificationservice.dto.request.AnswerCreatedRequest;
import com.team3.notificationservice.dto.response.NotificationResponse;
import com.team3.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
@Tag(name = "알림 API", description = "QnA 답변 알림 생성 및 조회 서비스")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "[알림 생성] 답변 등록 시 알림 생성",
            description = "트레이너가 질문에 답변을 등록하면 질문 작성자에게 알림을 생성합니다. (내부 서비스 호출용)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "내부 시크릿 키 불일치")
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/answer-created")
    public ResponseEntity<Void> answerCreated(@RequestBody AnswerCreatedRequest request) {
        notificationService.handleAnswerCreated(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "[알림 조회] 사용자 전체 알림 목록",
            description = "특정 사용자의 모든 알림을 최신순으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @Parameter(description = "조회할 사용자 ID", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @Operation(
            summary = "[알림 조회] 사용자 미확인 알림 목록",
            description = "특정 사용자의 읽지 않은 알림만 최신순으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/users/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadUserNotifications(
            @Parameter(description = "조회할 사용자 ID", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @Operation(
            summary = "[알림 상태] 알림 읽음 처리",
            description = "특정 알림을 읽음 상태로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽음 처리 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 알림을 찾을 수 없음")
    })
    @SecurityRequirement(name = "JWT")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "읽음 처리할 알림 ID", required = true)
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
