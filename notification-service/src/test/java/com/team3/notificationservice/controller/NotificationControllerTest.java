package com.team3.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.notificationservice.dto.request.AnswerCreatedRequest;
import com.team3.notificationservice.dto.response.NotificationResponse;
import com.team3.notificationservice.interceptor.InternalSecretInterceptor;
import com.team3.notificationservice.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@DisplayName("NotificationController 단위 테스트")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private InternalSecretInterceptor internalSecretInterceptor;

    private final Long TEST_USER_ID = 1L;
    private final Long TEST_NOTIFICATION_ID = 100L;

    @Nested
    @DisplayName("POST /notification/answer-created")
    class AnswerCreatedTest {

        @Test
        @DisplayName("성공: 답변 알림 생성 요청")
        void success_createNotification() throws Exception {
            // given
            AnswerCreatedRequest request = AnswerCreatedRequest.builder()
                    .questionId(1L)
                    .answerId(10L)
                    .questionOwnerId(TEST_USER_ID)
                    .trainerId(2L)
                    .questionTitle("테스트 질문")
                    .build();

            doNothing().when(notificationService).handleAnswerCreated(any(AnswerCreatedRequest.class));
            given(internalSecretInterceptor.preHandle(any(), any(), any())).willReturn(true);

            // when & then - POST 요청으로 알림 생성 API가 정상 동작하는지 검증하는 테스트입니다
            mockMvc.perform(post("/notification/answer-created")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /notification/users/{userId}")
    class GetUserNotificationsTest {

        @Test
        @DisplayName("성공: 사용자 전체 알림 조회")
        void success_getUserNotifications() throws Exception {
            // given
            NotificationResponse response = NotificationResponse.builder()
                    .notificationId(TEST_NOTIFICATION_ID)
                    .userId(TEST_USER_ID)
                    .content("테스트 알림")
                    .sendByUserId(2L)
                    .checkNotification(false)
                    .build();

            given(notificationService.getUserNotifications(TEST_USER_ID)).willReturn(List.of(response));
            given(internalSecretInterceptor.preHandle(any(), any(), any())).willReturn(true);

            // when & then - 응답 JSON에 알림 정보가 올바르게 포함되는지 검증하는 테스트입니다
            mockMvc.perform(get("/notification/users/{userId}", TEST_USER_ID))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].notificationId").value(TEST_NOTIFICATION_ID))
                    .andExpect(jsonPath("$[0].userId").value(TEST_USER_ID))
                    .andExpect(jsonPath("$[0].content").value("테스트 알림"));
        }
    }

    @Nested
    @DisplayName("GET /notification/users/{userId}/unread")
    class GetUnreadNotificationsTest {

        @Test
        @DisplayName("성공: 미확인 알림만 조회")
        void success_getUnreadNotifications() throws Exception {
            // given
            NotificationResponse response = NotificationResponse.builder()
                    .notificationId(TEST_NOTIFICATION_ID)
                    .userId(TEST_USER_ID)
                    .content("읽지 않은 알림")
                    .sendByUserId(2L)
                    .checkNotification(false)
                    .build();

            given(notificationService.getUnreadNotifications(TEST_USER_ID)).willReturn(List.of(response));
            given(internalSecretInterceptor.preHandle(any(), any(), any())).willReturn(true);

            // when & then
            mockMvc.perform(get("/notification/users/{userId}/unread", TEST_USER_ID))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].checkNotification").value(false));
        }
    }

    @Nested
    @DisplayName("PATCH /notification/{notificationId}/read")
    class MarkAsReadTest {

        @Test
        @DisplayName("성공: 알림 읽음 처리")
        void success_markAsRead() throws Exception {
            // given
            doNothing().when(notificationService).markAsRead(anyLong());
            given(internalSecretInterceptor.preHandle(any(), any(), any())).willReturn(true);

            // when & then
            mockMvc.perform(patch("/notification/{notificationId}/read", TEST_NOTIFICATION_ID))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}
