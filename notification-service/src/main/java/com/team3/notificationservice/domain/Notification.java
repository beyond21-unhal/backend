package com.team3.notificationservice.domain;

import com.team3.notificationservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;   // PK AutoIncrement

    @Column(nullable = false)
    private Long userId;           // 알림 받는 유저(질문 작성자) ID

    @Column(nullable = false, length = 500)
    private String content;        // 알림 내용

    @Column(nullable = false)
    private Long sendByUserId;     // 알림 보낸 사람(트레이너) ID

    @Column(nullable = false)
    private Boolean checkNotification = Boolean.FALSE; // 알림 확인 여부 (기본 false)

    @Builder
    public Notification(Long userId, String content, Long sendByUserId) {
        this.userId = userId;
        this.content = content;
        this.sendByUserId = sendByUserId;
        this.checkNotification = Boolean.FALSE;
    }

    public void markAsRead() {
        this.checkNotification = Boolean.TRUE;
    }
}
