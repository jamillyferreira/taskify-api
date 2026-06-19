package com.ferreira.taskify_api.dto.response;

public record RegisterResponseDTO(
        Long id,
        String name,
        String email
) {
}
