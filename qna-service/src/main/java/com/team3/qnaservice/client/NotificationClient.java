package com.team3.qnaservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.team3.qnaservice.dto.request.AnswerCreatedRequest;


@FeignClient(name = "notification-service", url = "${notification.service.url}")
public interface NotificationClient {

    @PostMapping("/notification/answer-created")
    void sendAnswerNotification(@RequestBody AnswerCreatedRequest request);
}
