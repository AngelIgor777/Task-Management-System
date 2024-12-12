package org.task.task_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.request.RegisterRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.service.AuthService;
import org.task.task_management_system.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest, Role.RoleName.ROLE_USER);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // Вызов метода для аутентификации пользователя
        AuthResponse authResponse = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }
}
