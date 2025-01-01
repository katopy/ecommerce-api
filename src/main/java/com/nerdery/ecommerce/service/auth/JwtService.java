package com.nerdery.ecommerce.service.auth;

import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final Set<String> blacklist = new HashSet<>();

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user, Map<String, Object> extraClaims){
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime());
        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .id(UUID.randomUUID().toString())
                .claim("jti", UUID.randomUUID().toString())
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    public SecretKey generateKey(){
        byte[] passwordDecoded = Decoders.BASE64.decode(SECRET_KEY);
        System.out.println(new String(passwordDecoded));
        return Keys.hmacShaKeyFor(passwordDecoded);
    }

    public String extractClaim(String jwt){return extractAllClaims(jwt).getSubject();}

    public Claims extractAllClaims(String jwt){
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public String extractJwtFromReq(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String TOKEN_PREFIX = "Bearer ";
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return authHeader.split(" ")[1];
    }
//    public boolean manageBlacklist(String token, boolean addToBlacklist) {
//        if (addToBlacklist) {
//            blacklist.add(token);
//            return true;
//        }
//        return blacklist.contains(token);
//    }

}
