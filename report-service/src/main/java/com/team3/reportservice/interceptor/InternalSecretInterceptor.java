package com.team3.reportservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@Slf4j
public class InternalSecretInterceptor implements HandlerInterceptor {
    private final String internalSecret;

    // 생성자 주입으로 yml 값을 가져옵니다. (Spring @Value 사용)
    public InternalSecretInterceptor(@Value("${internal.secret-key}") String internalSecret) {
        this.internalSecret = internalSecret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 요청 헤더에서 키 꺼내기
        String headerKey = request.getHeader("X-Internal-Secret");

        // 2. 키가 없거나, 내 비밀키랑 다르면 -> 차단 (403 Forbidden)
        if (headerKey == null || !Objects.equals(headerKey, internalSecret)) {
            log.warn("Blocked access with invalid secret key. Header: {}", headerKey);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Internal Secret");
            return false; // 컨트롤러로 못 가게 막음
        }

        // 3. 일치하면 통과
        return true;
    }
}
