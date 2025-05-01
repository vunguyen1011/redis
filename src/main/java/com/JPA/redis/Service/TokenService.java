package com.JPA.redis.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;



    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenTTL;





    public void saveRefreshToken(String username, String jti) {
        String key = "refreshToken::" + username;
        redisTemplate.opsForValue().set(key, jti, Duration.ofSeconds(refreshTokenTTL));
    }

    public boolean isRefreshTokenValid(String username, String jti) {
        String storedJti = (String) redisTemplate.opsForValue().get("refreshToken::" + username);
        return jti.equals(storedJti);
    }

    public void revokeRefreshToken(String username) {
        redisTemplate.delete("refreshToken::" + username);
    }
    public void blacklistToken(String jti, long ttlMillis) {
        redisTemplate.opsForValue().set("blacklist::" + jti, true, Duration.ofMillis(ttlMillis));
    }
    public boolean isTokenBlacklisted(String jti) {
        return redisTemplate.hasKey("blacklist::" + jti);
    }
}
