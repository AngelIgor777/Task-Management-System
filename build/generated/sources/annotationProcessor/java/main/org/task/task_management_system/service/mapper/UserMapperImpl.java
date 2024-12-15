package org.task.task_management_system.service.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.task.task_management_system.dto.request.AuthRequest;
import org.task.task_management_system.dto.response.AuthResponse;
import org.task.task_management_system.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T22:57:04+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.11.1.jar, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public AuthResponse toAuthResponse(User user) {
        if ( user == null ) {
            return null;
        }

        AuthResponse authResponse = new AuthResponse();

        return authResponse;
    }

    @Override
    public User toUser(AuthRequest authRequest) {
        if ( authRequest == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
