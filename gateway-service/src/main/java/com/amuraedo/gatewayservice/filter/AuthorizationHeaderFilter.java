package com.amuraedo.gatewayservice.filter;

import com.amuraedo.gatewayservice.util.GatewayRedisUtil;
import com.amuraedo.gatewayservice.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtUtil jwtUtil;
    private final GatewayRedisUtil redisUtil;

    @Value("${internal.secret-key}")
    private String internalSecretKey;

    // ⭕ 생성자를 직접 만들고, super(Config.class)를 꼭 호출해야 합니다!
    public AuthorizationHeaderFilter(JwtUtil jwtUtil, GatewayRedisUtil redisUtil) {
        super(Config.class); // ⭐ 핵심: 부모에게 "내 설정 클래스는 Config야"라고 알려줌
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    public static class Config { }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 헤더 유무 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = authHeeader.replace("Bearer ", "");

            if(!jwtUtil.validateToken(token)){
                return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
            }

            return redisUtil.hasBlackList(token)
                    .flatMap(isBlackListed -> {
                        if(isBlackListed){
                            return onError(exchange, "Logout Token (Blacklist)", HttpStatus.UNAUTHORIZED);
                        }

                        String userId = jwtUtil.getUserId(token);
                        String role = jwtUtil.getRole(token);

                        ServerHttpRequest modifiedRequest = request.mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .header("X-Internal-Secret", internalSecretKey)
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    });


        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        log.error("Gateway Filter Error: {}", message);
        return response.setComplete();
    }

}
