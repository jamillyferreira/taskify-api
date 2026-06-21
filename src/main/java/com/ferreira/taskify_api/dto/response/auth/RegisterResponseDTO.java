package com.ferreira.taskify_api.dto.response.auth;

public record RegisterResponseDTO(
        Long id,
        String name,
        String email
) {
}
