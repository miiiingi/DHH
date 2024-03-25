package study.deliveryhanghae.global.config.security.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.time.Duration;

@Service
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 의존성 주입
    public TokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRefreshToken(String refreshToken, String email) {
        // Refresh 토큰을 Redis에 저장
        redisTemplate.opsForValue().set(refreshToken, email, Duration.ofDays(3)); // 3일간 유효
    }

    public String getRefreshToken(String email) {
        return String.valueOf(redisTemplate.opsForValue().get(email));
    }

    public void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    public void deleteRefreshToken(String email) {
        String refreshToken = getRefreshToken(email);
        // Refresh 토큰 삭제
        redisTemplate.delete(refreshToken);
    }
}