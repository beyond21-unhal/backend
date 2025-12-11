package com.team3.notificationservice.repository;

import com.team3.notificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 유저의 모든 알림 조회 (최신순)
    List<Notification> findAllByUserIdOrderByNotificationIdDesc(Long userId);

    // 특정 유저의 읽지 않은 알림 조회 (최신순)
    List<Notification> findAllByUserIdAndCheckNotificationFalseOrderByNotificationIdDesc(Long userId);
}