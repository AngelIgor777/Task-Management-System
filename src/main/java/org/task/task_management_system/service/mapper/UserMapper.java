package org.task.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AuthResponse toAuthResponse(User user);
    User toUser(AuthRequest authRequest);
}
