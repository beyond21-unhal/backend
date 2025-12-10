package com.team3.notificationservice.dto.request;


import com.team3.notificationservice.domain.Notification;

public record NotificationDTO(
        Long userID ,
        String content ,
        Long sendByUserId

)
{
    public Notification toEntity(){
        return Notification.builder()
                .userId(userID)
                .content(content)
                .sendByUserID(sendByUserId)
                .build();
    }




}
