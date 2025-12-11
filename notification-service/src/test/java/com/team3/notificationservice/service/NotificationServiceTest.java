package com.team3.notificationservice.service;

import com.team3.notificationservice.domain.Notification;
import com.team3.notificationservice.dto.request.AnswerCreatedRequest;
import com.team3.notificationservice.dto.response.NotificationResponse;
import com.team3.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService 단위 테스트")
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification testNotification;
    private final Long TEST_USER_ID = 1L;
    private final Long TEST_TRAINER_ID = 2L;
    private final Long TEST_NOTIFICATION_ID = 100L;

    @BeforeEach
    void setUp() {
        testNotification = Notification.builder()
                .userId(TEST_USER_ID)
                .sendByUserId(TEST_TRAINER_ID)
                .content("질문 [테스트 질문]에 답변이 등록되었습니다.")
                .build();
    }

    @Nested
    @DisplayName("handleAnswerCreated - 답변 알림 생성")
    class HandleAnswerCreatedTest {

        @Test
        @DisplayName("성공: 답변 생성 시 알림이 저장된다")
        void success_saveNotification() {
            // given
            AnswerCreatedRequest request = AnswerCreatedRequest.builder()
                    .questionId(1L)
                    .answerId(10L)
                    .questionOwnerId(TEST_USER_ID)
                    .trainerId(TEST_TRAINER_ID)
                    .questionTitle("테스트 질문")
                    .build();

            given(notificationRepository.save(any(Notification.class))).willReturn(testNotification);

            // when
            notificationService.handleAnswerCreated(request);

            // then - 알림이 올바른 수신자/발신자/내용으로 저장되는지 검증하는 테스트입니다
            ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
            verify(notificationRepository).save(captor.capture());

            Notification saved = captor.getValue();
            assertThat(saved.getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(saved.getSendByUserId()).isEqualTo(TEST_TRAINER_ID);
            assertThat(saved.getContent()).contains("테스트 질문");
            assertThat(saved.getCheckNotification()).isFalse();
        }
    }

    @Nested
    @DisplayName("getUserNotifications - 전체 알림 조회")
    class GetUserNotificationsTest {

        @Test
        @DisplayName("성공: 사용자의 모든 알림을 조회한다")
        void success_getAllNotifications() {
            // given
            List<Notification> notifications = List.of(testNotification);
            given(notificationRepository.findAllByUserIdOrderByNotificationIdDesc(TEST_USER_ID))
                    .willReturn(notifications);

            // when
            List<NotificationResponse> result = notificationService.getUserNotifications(TEST_USER_ID);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).userId()).isEqualTo(TEST_USER_ID);
        }

        @Test
        @DisplayName("성공: 알림이 없으면 빈 리스트를 반환한다")
        void success_emptyList() {
            // given
            given(notificationRepository.findAllByUserIdOrderByNotificationIdDesc(TEST_USER_ID))
                    .willReturn(List.of());

            // when
            List<NotificationResponse> result = notificationService.getUserNotifications(TEST_USER_ID);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getUnreadNotifications - 미확인 알림 조회")
    class GetUnreadNotificationsTest {

        @Test
        @DisplayName("성공: 읽지 않은 알림만 조회한다")
        void success_getUnreadOnly() {
            // given
            List<Notification> unreadNotifications = List.of(testNotification);
            given(notificationRepository.findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc(TEST_USER_ID))
                    .willReturn(unreadNotifications);

            // when
            List<NotificationResponse> result = notificationService.getUnreadNotifications(TEST_USER_ID);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).checkNotification()).isFalse();
        }
    }

    @Nested
    @DisplayName("markAsRead - 알림 읽음 처리")
    class MarkAsReadTest {

        @Test
        @DisplayName("성공: 알림을 읽음 상태로 변경한다")
        void success_markAsRead() {
            // given
            given(notificationRepository.findById(TEST_NOTIFICATION_ID))
                    .willReturn(Optional.of(testNotification));

            // when
            notificationService.markAsRead(TEST_NOTIFICATION_ID);

            // then - 더티체킹으로 checkNotification이 true로 변경되는지 검증하는 테스트입니다
            assertThat(testNotification.getCheckNotification()).isTrue();
        }

        @Test
        @DisplayName("실패: 존재하지 않는 알림 ID로 요청 시 예외 발생")
        void fail_notificationNotFound() {
            // given
            given(notificationRepository.findById(TEST_NOTIFICATION_ID))
                    .willReturn(Optional.empty());

            // when & then - 존재하지 않는 알림 조회 시 예외 처리가 정상 동작하는지 검증하는 테스트입니다
            assertThatThrownBy(() -> notificationService.markAsRead(TEST_NOTIFICATION_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 알림을 찾을 수 없습니다");
        }
    }
}
