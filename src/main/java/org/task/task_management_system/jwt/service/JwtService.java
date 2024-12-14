package org.task.task_management_system.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.task.task_management_system.service.CustomUserDetailsService;
import org.task.task_management_system.util.JwtAlgorithm;
import org.task.task_management_system.util.KeyUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {


    private final HttpServletRequest request;

    private final CustomUserDetailsService customUserDetailsService;

    public String generateAccessToken(String username, List<String> roles) {
        String accessExpiration = KeyUtil.getAccessExpiration();

        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles.stream().map(role -> "ROLE_" + role).toList())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(accessExpiration)))
                .sign(JwtAlgorithm.getAccessAlgorithm());
    }

    public String generateRefreshToken(String username) {
        String refreshExpiration = KeyUtil.getRefreshExpiration();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpiration))) // 7 days
                .sign(JwtAlgorithm.getRefreshAlgorithm());
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(JwtAlgorithm.getAccessAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }


    public boolean isTokenValid(String token, Algorithm algorithm) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getExpiresAt().after(new java.util.Date());
        } catch (JWTVerificationException e) {
            return false;
        }
    }


    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String userEmail = decodedJWT.getSubject();

        if (userEmail == null) {
            return null;
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.info("Authorities: " + authorities);
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                authorities
        );
    }

    public String extractToken() {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("JWT not exist");
    }
}