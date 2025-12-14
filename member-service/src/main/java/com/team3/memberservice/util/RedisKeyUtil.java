package com.team3.memberservice.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class RedisKeyUtil {

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BALCKLIST_PREFIX = "BL:";

    public static String getRefreshTokenKey(Long userId){
        return REFRESH_TOKEN_PREFIX + userId;
    }

    public static String getBlackListKey(String accessToken){
        return BALCKLIST_PREFIX + accessToken;
    }

}
