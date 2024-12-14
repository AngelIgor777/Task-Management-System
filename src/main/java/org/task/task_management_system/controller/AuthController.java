package org.task.task_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.request.RegisterRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.service.AuthService;
import org.task.task_management_system.service.UserService;
import org.task.task_management_system.util.KeyUtil;

@Tag(name = "Authentication Controller", description = "API for user authentication and registration")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user with the role USER",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully")
            })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {

        System.out.println("Received registration request: " + registerRequest);

        userService.registerUser(registerRequest, Role.RoleName.USER);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Login an existing user",
            description = "Authenticates a user and provides a JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token issued successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials")
            })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Assign ADMIN role to a user",
            description = "Assigns the ADMIN role to a user if the super-admin key matches",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role ADMIN assigned successfully")
            })
    @PostMapping("/addAdminRole/{userId}")
    public ResponseEntity<String> addAdminRole(@PathVariable Long userId, @RequestHeader("super-admin-key") String superAdminKey) {
        if (superAdminKey.equals(KeyUtil.getSuperAdminKey())) {
            userService.addRoleToUser(userId, Role.RoleName.ADMIN);
            return ResponseEntity.ok("Role ADMIN assigned successfully");
        } else {
            return ResponseEntity.status(403).body("Super admin key does not match");
        }
    }
}
