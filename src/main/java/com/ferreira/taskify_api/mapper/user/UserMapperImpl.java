package com.ferreira.taskify_api.mapper.user;

import com.ferreira.taskify_api.dto.request.auth.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.auth.RegisterResponseDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        return user;
    }

    @Override
    public RegisterResponseDTO toRegisterResponseDTO(User user) {
        return new RegisterResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
