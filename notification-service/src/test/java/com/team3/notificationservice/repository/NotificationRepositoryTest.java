package com.team3.notificationservice.repository;

import com.team3.notificationservice.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("NotificationRepository 통합 테스트")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private final Long TEST_USER_ID = 1L;
    private final Long TEST_TRAINER_ID = 2L;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @Nested
    @DisplayName("findAllByUserIdOrderByNotificationIdDesc")
    class FindAllByUserIdTest {

        @Test
        @DisplayName("성공: 사용자의 모든 알림을 최신순으로 조회한다")
        void success_findAllByUserId() {
            // given
            Notification notification1 = createNotification("첫 번째 알림", false);
            Notification notification2 = createNotification("두 번째 알림", true);
            notificationRepository.saveAll(List.of(notification1, notification2));

            // when
            List<Notification> result = notificationRepository
                    .findAllByUserIdOrderByNotificationIdDesc(TEST_USER_ID);

            // then - 알림이 최신순(ID 내림차순)으로 정렬되는지 검증하는 테스트입니다
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getNotificationId())
                    .isGreaterThan(result.get(1).getNotificationId());
        }

        @Test
        @DisplayName("성공: 다른 사용자의 알림은 조회되지 않는다")
        void success_notIncludeOtherUserNotifications() {
            // given
            Notification myNotification = createNotification("내 알림", false);
            Notification otherNotification = Notification.builder()
                    .userId(999L)
                    .sendByUserId(TEST_TRAINER_ID)
                    .content("다른 사용자 알림")
                    .build();
            notificationRepository.saveAll(List.of(myNotification, otherNotification));

            // when
            List<Notification> result = notificationRepository
                    .findAllByUserIdOrderByNotificationIdDesc(TEST_USER_ID);

            // then - 다른 사용자의 알림이 조회되지 않는지 검증하는 테스트입니다
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUserId()).isEqualTo(TEST_USER_ID);
        }
    }

    @Nested
    @DisplayName("findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc")
    class FindUnreadNotificationsTest {

        @Test
        @DisplayName("성공: 읽지 않은 알림만 조회한다")
        void success_findUnreadOnly() {
            // given
            Notification unreadNotification = createNotification("읽지 않은 알림", false);
            Notification readNotification = createNotification("읽은 알림", false);
            readNotification.markAsRead();
            notificationRepository.saveAll(List.of(unreadNotification, readNotification));

            // when
            List<Notification> result = notificationRepository
                    .findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc(TEST_USER_ID);

            // then - checkNotification=false인 알림만 필터링되는지 검증하는 테스트입니다
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCheckNotification()).isFalse();
        }

        @Test
        @DisplayName("성공: 모든 알림을 읽었으면 빈 리스트를 반환한다")
        void success_emptyWhenAllRead() {
            // given
            Notification readNotification = createNotification("읽은 알림", false);
            readNotification.markAsRead();
            notificationRepository.save(readNotification);

            // when
            List<Notification> result = notificationRepository
                    .findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc(TEST_USER_ID);

            // then
            assertThat(result).isEmpty();
        }
    }

    private Notification createNotification(String content, boolean isRead) {
        Notification notification = Notification.builder()
                .userId(TEST_USER_ID)
                .sendByUserId(TEST_TRAINER_ID)
                .content(content)
                .build();
        if (isRead) {
            notification.markAsRead();
        }
        return notification;
    }
}
