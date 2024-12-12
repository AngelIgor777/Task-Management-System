package org.task.task_management_system.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.util.JwtAlgorithm;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/api/v1/**")
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtService.resolveToken(request);
        if (token != null && jwtService.isTokenValid(token, JwtAlgorithm.getAccessAlgorithm())) {
            SecurityContextHolder.getContext().setAuthentication(jwtService.getAuthentication(token));
        }
        filterChain.doFilter(request, response);
    }
}
