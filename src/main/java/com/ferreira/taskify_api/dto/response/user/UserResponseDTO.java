package com.ferreira.taskify_api.dto.response.user;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
