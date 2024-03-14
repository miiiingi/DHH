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
import study.deliveryhanghae.domain.user.dto.LoginRequestRecord;
import study.deliveryhanghae.domain.user.entity.UserRoleEnum;
import study.deliveryhanghae.global.config.security.UserDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.IOException;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
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
        log.info("로그인 성공 및 JWT 생성");
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getAuthority();

        String token = jwtUtil.createToken(email, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setCharacterEncoding("UTF-8");
        String responseDto = new ObjectMapper().writeValueAsString(Map.of("data", "BAD_REQUEST", "message", "로그인 실패", "resultCode", HttpServletResponse.SC_BAD_REQUEST));
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write(responseDto);
        response.getWriter().flush();
        response.getWriter().close();
    }
}