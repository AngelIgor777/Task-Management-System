package org.task.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.task_management_system.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
