package com.ferreira.taskify_api.mapper.user;

import com.ferreira.taskify_api.dto.request.auth.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.auth.RegisterResponseDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.model.User;

public interface UserMapper {
   User toEntity(RegisterRequestDTO dto);

   RegisterResponseDTO toRegisterResponseDTO(User user);

   UserResponseDTO toUserResponseDTO(User user);
}
