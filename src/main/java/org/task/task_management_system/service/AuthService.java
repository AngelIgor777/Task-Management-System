package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.service.mapper.UserMapper;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        ArrayList<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.toString());
        String accessToken = jwtService.generateAccessToken(authRequest.getEmail(), roles);
        String refreshToken = jwtService.generateRefreshToken(authRequest.getEmail());

        return new AuthResponse(accessToken, refreshToken, user.getId().toString());
    }
}
