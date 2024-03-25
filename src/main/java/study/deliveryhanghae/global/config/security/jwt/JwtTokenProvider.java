package study.deliveryhanghae.global.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");
    // Header Key
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN = "Refresh";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final TokenService tokenService;

    public JwtTokenProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // 보안 문제로 변경 : signWith(SignatureAlgorithm, java.lang.String) -> signWith(Key key)
    private Key getSigningKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String email){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email) // 사용자 식별자값(ID)
                .claim("email", email)
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
        return BEARER_PREFIX + Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token, String email, HttpServletResponse res) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            // access Token 만료시 재발급
            String refreshToken = resolveToken(tokenService.getRefreshToken(email));
            tokenService.verifiedRefreshToken(refreshToken);
            token = createAccessToken(email);
            res.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public String getAccessTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        return null;
    }

}
