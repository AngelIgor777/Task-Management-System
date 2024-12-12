package org.task.task_management_system.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.task.task_management_system.util.JwtAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtService {


    public String generateAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .sign(JwtAlgorithm.getAccessAlgorithm());
    }

    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 604800000)) // 7 days
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
        return new UsernamePasswordAuthenticationToken(userEmail, null, null);
    }
}