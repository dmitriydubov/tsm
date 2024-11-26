package com.example.tms.security.service;

import com.example.tms.security.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByRefreshToken(String token);
    RefreshToken createRefreshToken(Long userId);
    RefreshToken checkRefreshToken(RefreshToken token);
    void deleteByUserId(Long id);
}