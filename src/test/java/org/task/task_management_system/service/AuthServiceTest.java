package org.task.task_management_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authRequest = new AuthRequest("test@example.com", "password");
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void authenticateUser_Success() {
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtService.generateAccessToken(anyString(), anyList()))
                .thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString()))
                .thenReturn("refreshToken");
        AuthResponse authResponse = authService.authenticateUser(authRequest);

        assertNotNull(authResponse);
        assertEquals("accessToken", authResponse.getAccessToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());
        assertEquals("1", authResponse.getUserId());
    }

    @Test
    void authenticateUser_UserNotFound() {
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(authRequest);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void authenticateUser_InvalidPassword() {
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticateUser(authRequest);
        });
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
