package com.expencetracker.services;

import com.expencetracker.entities.RefreshToken;
import com.expencetracker.entities.UserInfo;
import com.expencetracker.repositories.RefreshTokenRepository;
import com.expencetracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String userName) {
        UserInfo userNameExtracted = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User not found!!"));
        RefreshToken refreshToken = RefreshToken.builder().userInfo(userNameExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(7L * 24 * 60 * 60))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate() == null || !refreshToken.getExpiryDate().isAfter(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh token expired, Please make a new login!!");
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }
}
