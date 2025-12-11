package com.team3.notificationservice.service;

import com.team3.notificationservice.domain.Notification;
import com.team3.notificationservice.dto.request.AnswerCreatedRequest;
import com.team3.notificationservice.dto.response.NotificationResponse;
import com.team3.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 답변 생성 알림 저장
    @Transactional
    public void handleAnswerCreated(AnswerCreatedRequest request) {
        // 알림 메시지 포맷팅
        String message = String.format("질문 [%s]에 답변이 등록되었습니다.", request.questionTitle());

        Notification notification = Notification.builder()
                .userId(request.questionOwnerId())  // 질문 작성자에게 알림
                .sendByUserId(request.trainerId())  // 답변 작성자(트레이너)
                .content(message)
                .build();

        notificationRepository.save(notification);
        log.info("Notification saved for userId: {}", request.questionOwnerId());
    }

    // 유저 알림 전체 조회
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findAllByUserIdOrderByNotificationIdDesc(userId)
                .stream()
                .map(NotificationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 읽지 않은 알림 조회
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc(userId)
                .stream()
                .map(NotificationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다."));

        notification.markAsRead(); // checkNotification = true 로 변경
    }
}