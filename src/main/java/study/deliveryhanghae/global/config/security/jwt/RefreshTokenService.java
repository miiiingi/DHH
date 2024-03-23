package study.deliveryhanghae.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(String email, String where, String refreshToken, String accessToken) {
        refreshTokenRepository.save(new RefreshToken(email, where, refreshToken, accessToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
