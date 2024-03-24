package study.deliveryhanghae.global.config.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import study.deliveryhanghae.global.config.security.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtTokenProvider.getTokenFromRequest(req);

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtTokenProvider.resolveToken(tokenValue);

            if (!jwtTokenProvider.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtTokenProvider.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createUserAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createUserAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}