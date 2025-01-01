package com.nerdery.ecommerce.config.security.filter;

import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.service.RevokedTokenService;
import com.nerdery.ecommerce.service.UserService;
import com.nerdery.ecommerce.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final RevokedTokenService blacklist;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.extractJwtFromReq(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            claims = jwtService.extractAllClaims(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
            return;
        }

        String email = claims.getSubject();
        String jti = claims.getId();

        if (blacklist.isBlacklisted(token) || blacklist.isRevoked(jti)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or revoked token.");
            return;
        }

//        String token = jwtService.extractJwtFromReq(request);
//        if (token != null) {
//            Claims claims = jwtService.extractAllClaims(token);
//            String jti = claims.getId();
//            if (blacklist.isBlacklisted(token) || blacklist.isRevoked(jti)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid or revoked token.");
//                return;
//            }
//        }
        UserDetails user = userService.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with email: " + email));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                email, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);

    }
}
