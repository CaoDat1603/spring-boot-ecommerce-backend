package com.dat.ecommerce.service;

import com.dat.ecommerce.entity.RefreshToken;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiresAt(
                LocalDateTime.now()
                        .plusSeconds(refreshExpiration / 1000)
        );

        refreshToken.setRevoked(false);

        refreshToken.setCreatedAt(
                LocalDateTime.now()
        );

        return refreshTokenRepository.save(
                refreshToken
        );
    }

    @Transactional(readOnly = true)
    public RefreshToken findByToken(
            String token
    ) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Refresh token not found"
                        )
                );
    }

    @Transactional(readOnly = true)
    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ) {

        if (refreshToken.getRevoked()) {

            throw new IllegalStateException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            refreshToken.setRevoked(true);

            refreshTokenRepository.save(
                    refreshToken
            );

            throw new IllegalStateException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    @Transactional
    public void revokeToken(
            RefreshToken refreshToken
    ) {

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(
                refreshToken
        );
    }
}