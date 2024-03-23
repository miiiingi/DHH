package study.deliveryhanghae.global.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import study.deliveryhanghae.global.config.redis.RedisDao;
import study.deliveryhanghae.global.config.security.UserDetailsServiceImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth); // 정상 토큰이면 SecurityContext에 저장
            }
        } catch (RedisConnectionFailureException e) {
            SecurityContextHolder.clearContext();
            throw new BusinessException(ErrorCode.REDIS_ERROR);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        filterChain.doFilter(request, response);
    }
}