package com.ferreira.taskify_api.mapper.user;

import com.ferreira.taskify_api.dto.request.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.RegisterResponseDTO;
import com.ferreira.taskify_api.dto.response.UserResponseDTO;
import com.ferreira.taskify_api.model.User;

public interface UserMapper {
   User toEntity(RegisterRequestDTO dto);

   RegisterResponseDTO toRegisterResponseDTO(User user);

   UserResponseDTO toUserResponseDTO(User user);
}
