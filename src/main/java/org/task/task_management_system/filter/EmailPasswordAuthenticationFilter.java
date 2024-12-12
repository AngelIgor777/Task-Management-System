package org.task.task_management_system.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.task.task_management_system.dto.request.AuthRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmailPasswordAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth == null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                AuthRequest loginRequest = objectMapper.readValue(request.getInputStream(), AuthRequest.class);

                String email = loginRequest.getEmail();
                String password = loginRequest.getPassword();

                if (email != null && password != null) {
                    // Создание токена для аутентификации
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, password);

                    // Установка деталей запроса
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Аутентификация
                    Authentication authentication = authenticationManager.authenticate(authenticationToken);

                    // Установка контекста безопасности
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                }
            } catch (Exception e) {
                // Логирование ошибок аутентификации (необязательно)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
