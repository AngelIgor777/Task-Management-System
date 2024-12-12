package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        // Проверяем существует ли пользователь с таким email
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверяем правильность пароля
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Генерация JWT токенов
        String accessToken = jwtService.generateAccessToken(authRequest.getEmail());
        String refreshToken = jwtService.generateRefreshToken(authRequest.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }
}
