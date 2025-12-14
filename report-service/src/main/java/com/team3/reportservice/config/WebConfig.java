package com.team3.reportservice.config;

import com.team3.reportservice.interceptor.InternalSecretInterceptor;
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
                .addPathPatterns("/report/**"); // 모든 API 검사
        // 보통은 /api/auth/** 도 게이트웨이를 통한다면 검사하는 게 맞습니다.
        // internal secret은 게이트웨이가 무조건 붙여주니까요.
    }
}
