package com.nerdery.ecommerce.service;

import jakarta.servlet.http.HttpServletRequest;

public interface RevokedTokenService {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
    boolean isRevoked(String jti);
    void addRevokeToken(HttpServletRequest req);
}
