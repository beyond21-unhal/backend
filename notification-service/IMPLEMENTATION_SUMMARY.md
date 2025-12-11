# Notification Service 구현 정리요약본


---

## 📡 API 엔드포인트

| Method | Endpoint | 헤더/Body | 설명 |
|--------|----------|-----------|------|
| POST | `/notification/answer-created` | Body: AnswerCreatedRequest | 답변 등록 시 알림 생성 (내부 서비스 호출용) |
| GET | `/notification` | Header: X-User-Id | 내 전체 알림 조회 |
| GET | `/notification/unread` | Header: X-User-Id | 내 미확인 알림만 조회 |
| PATCH | `/notification/read` | Header: X-User-Id, Body: MarkAsReadRequest | 알림 읽음 처리 |

### 팀 API 협업 규칙 적용
- URL 경로에 개인 식별자(ID) 노출 금지
- 사용자 ID는 `@RequestHeader("X-User-Id")`로 수신 (Swagger에서 hidden 처리)
- 읽음 처리 API는 `@RequestBody`로 알림 ID 수신

---

## 🏗️ 전체 아키텍처 흐름

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────────────┐
│   Client    │────▶│  Gateway:8000   │────▶│ Notification Service│
│  (Swagger)  │     │                 │     │    (Random Port)    │
└─────────────┘     └─────────────────┘     └─────────────────────┘
                            │                         │
                            ▼                         ▼
                    ┌───────────────┐         ┌─────────────┐
                    │ Eureka:8761   │         │   MySQL DB  │
                    └───────────────┘         └─────────────┘
```

---

## 🔐 보안 흐름

```
1. Swagger UI에서 JWT 토큰 입력 (Authorize 버튼)
2. Gateway의 AuthorizationHeaderFilter가 토큰 검증
3. 검증 성공 시 헤더 추가:
   - X-User-Id: 사용자 ID
   - X-User-Role: 사용자 역할
   - X-Internal-Secret: 내부 통신용 시크릿 키
4. Notification Service의 InternalSecretInterceptor가 시크릿 키 검증
5. 검증 성공 시 Controller로 요청 전달
```

---

## 🗄️ 데이터 모델

### Notification Entity
```java
@Entity
public class Notification {
    Long notificationId;      // PK
    Long userId;              // 알림 수신자 (질문 작성자)
    Long sendByUserId;        // 알림 발신자 (트레이너)
    String content;           // 알림 내용
    Boolean checkNotification; // 읽음 여부 (기본: false)
}
```

---

## ✅ 테스트 방법

1. **서비스 실행 순서:**
   - Eureka Server (8761)
   - Member Service
   - Notification Service
   - Gateway Service (8000)

2. **Swagger 접속:** `http://localhost:8000/swagger`

3. **테스트 순서:**
   - Member 서비스에서 로그인 → JWT 토큰 획득
   - Authorize 버튼 클릭 → 토큰 입력
   - 알림 서비스 API 테스트
