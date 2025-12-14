package com.team3.dietplanservice.Interceptor;

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


    public InternalSecretInterceptor(@Value("${internal.secret-key}") String internalSecret) {
        this.internalSecret = internalSecret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String headerKey = request.getHeader("X-Internal-Secret");


        if (headerKey == null || !Objects.equals(headerKey, internalSecret)) {
            log.warn("Blocked access with invalid secret key. Header: {}", headerKey);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Internal Secret");
            return false;
        }


        return true;
    }
}
