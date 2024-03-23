package study.deliveryhanghae.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.deliveryhanghae.domain.user.dto.UserRequestDto.*;
import study.deliveryhanghae.global.config.security.owner.OwnerDetailsImpl;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestRecord requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestRecord.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(),
                            requestDto.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String accessToken = jwtTokenProvider.createAccessToken(authResult);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        if (authResult.getPrincipal() instanceof OwnerDetailsImpl) {
            refreshTokenService.saveTokenInfo(((OwnerDetailsImpl) authResult.getPrincipal()).getUser().getEmail(), "owner",refreshToken, accessToken);
        } else if (authResult.getPrincipal() instanceof UserDetailsImpl) {
            refreshTokenService.saveTokenInfo(((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail(), "user",refreshToken, accessToken);
        }

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 실패한 이유에 따라 적절한 에러 코드를 설정합니다.
        String errorCode = ErrorCode.NOT_MATCH_EMAIL_PASSWORD.getCode();
        String errorMessage = ErrorCode.NOT_MATCH_EMAIL_PASSWORD.getMessage();

        // 에러 코드와 메시지를 JSON 형태로 응답합니다.
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 예시로 400 상태코드를 설정합니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"ErrorCode\":\"" + errorCode + "\", \"ErrorMessage\":\"" + errorMessage + "\"}");
    }
}