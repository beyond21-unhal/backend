package com.team3.notificationservice.config;

import com.team3.notificationservice.interceptor.InternalSecretInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final InternalSecretInterceptor internalSecretInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalSecretInterceptor)
                .addPathPatterns("/notification") // 모든 API 검사
                .excludePathPatterns("/auth/**"); // (선택) 로그인은 게이트웨이 필터 안 거칠 수도 있으니 예외 처리 필요할 수도 있음
        // 보통은 /api/auth/** 도 게이트웨이를 통한다면 검사하는 게 맞습니다.
        // internal secret은 게이트웨이가 무조건 붙여주니까요.
    }

}