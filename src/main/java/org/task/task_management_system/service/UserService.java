package org.task.task_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.task.task_management_system.dto.request.RegisterRequest;
import org.task.task_management_system.entity.Role;
import org.task.task_management_system.entity.User;
import org.task.task_management_system.repository.RoleRepository;
import org.task.task_management_system.repository.UserRepository;

import javax.transaction.Transactional;

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

        Role roleBuild = Role.builder().name(roleName.name()).build();
        user.getRoles().add(roleBuild);
        roleRepository.save(roleBuild);

        return userRepository.save(user);
    }
}
