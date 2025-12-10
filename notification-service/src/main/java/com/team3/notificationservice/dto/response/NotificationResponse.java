package com.team3.notificationservice.dto.response;

import com.team3.notificationservice.domain.Notification;
import lombok.Builder;

public record NotificationResponse(
        Long notificationId,
        Long userId,
        String content,
        Long sendByUserId,
        Boolean checkNotification
) {

    @Builder
    public NotificationResponse { }

    public static NotificationResponse fromEntity(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .content(notification.getContent())
                .sendByUserId(notification.getSendByUserId())
                .checkNotification(notification.getCheckNotification())
                .build();
    }
}
