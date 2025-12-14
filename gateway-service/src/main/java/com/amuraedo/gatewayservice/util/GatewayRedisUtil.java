package com.amuraedo.gatewayservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GatewayRedisUtil {
    private final ReactiveStringRedisTemplate reactiveRedisTemplate;

    public Mono<Boolean> hasBlackList(String token) {
        String key = RedisKeyUtil.getBalckListKey(token);
        return reactiveRedisTemplate.hasKey(key);
    }
}
