package com.team3.memberservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessTokenExpTime;

    // application.yml에서 jwt.secret과 jwt.expiration-time 값을 가져옴
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenExpTime
    ) {
        // 0.12.3 버전부터는 String 키를 직접 사용하지 않고 SecretKey 객체로 변환해야 함
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createToken(Long userId, String role) {
        Date now = new Date();

        return Jwts.builder()
                .subject(String.valueOf(userId))      // 사용자 ID (Subject)
                .claim("role", role)                  // 사용자 권한 (Custom Claim)
                .issuedAt(now)                        // 발급 시간
                .expiration(new Date(now.getTime() + accessTokenExpTime)) // 만료 시간
                .signWith(secretKey)                  // 암호화 키 (SecretKey 객체 필수)
                .compact();
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)  // setSigningKey(key) -> verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // parseClaimsJws(token) -> parseSignedClaims(token)
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload(); // getBody() -> getPayload()
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String token) {
        // 1. 토큰의 만료 시각을 가져온다 (팀장님이 짠 코드)
        Date expiration = parseClaims(token).getExpiration();

        // 2. 현재 시간을 가져온다
        long now = new Date().getTime();

        // 3. "만료 시각 - 현재 시간"을 빼서 '남은 시간'을 반환한다
        return (expiration.getTime() - now);
    }

}
