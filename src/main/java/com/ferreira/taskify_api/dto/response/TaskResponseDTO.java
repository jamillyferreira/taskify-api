package com.ferreira.taskify_api.dto.response;

import com.ferreira.taskify_api.enums.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String userName,
        String title,
        String description,
        boolean completed,
        LocalDate dueDate,
        Priority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
