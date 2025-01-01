package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.persistence.entity.TokenRevoked;
import com.nerdery.ecommerce.persistence.repository.JwtRepository;
import com.nerdery.ecommerce.service.RevokedTokenService;
import com.nerdery.ecommerce.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RevokedTokenServiceImpl implements RevokedTokenService {

    private final Set<String> blacklist = new HashSet<>();
    private final JwtRepository repository;
    private final JwtService jwtService;


    @Override
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    @Override
    public boolean isRevoked(String jti) {
        return repository.existsByJtiAndIsValidFalse(jti);
    }

    @Override
    public void addRevokeToken(HttpServletRequest req) {
        String token = jwtService.extractJwtFromReq(req);
        Claims claims = jwtService.extractAllClaims(token);
        String jti = claims.getId();
        TokenRevoked tokenRevoked = new TokenRevoked();
        tokenRevoked.setJti(jti);
        tokenRevoked.setUserId(claims.get("userId", Long.class));
        tokenRevoked.setExpires_at(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        tokenRevoked.setValid(false);
        repository.save(tokenRevoked);
        addToBlacklist(token);
    }
}
