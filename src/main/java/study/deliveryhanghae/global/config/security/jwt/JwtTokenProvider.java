package study.deliveryhanghae.global.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import study.deliveryhanghae.global.config.security.UserDetailsServiceImpl;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;
    private final UserDetailsServiceImpl userDetailsService;



    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 보안 문제로 변경 : signWith(SignatureAlgorithm, java.lang.String) -> signWith(Key key)
    private Key getSigningKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        // refresh Token 반환
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        // JWT 토큰에서 클레임을 파싱하기 위해 parserBuilder()를 사용
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

        String userPrincipal = claimsJws.getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
