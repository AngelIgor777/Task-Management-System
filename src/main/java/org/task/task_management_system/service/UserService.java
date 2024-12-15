package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.task.task_management_system.dto.request.RegisterRequest;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.repository.RoleRepository;
import org.task.task_management_system.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest, Role.RoleName roleName) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role roleBuild = Role.builder().name(Role.RoleName.USER).build();
        user.getRoles().add(roleBuild);
        roleRepository.save(roleBuild);

        return userRepository.save(user);
    }

    public void addRoleToUser(Long userId, Role.RoleName roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = Role.builder().name(roleName).build();
        user.getRoles().add(role);

        roleRepository.save(role);
        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
