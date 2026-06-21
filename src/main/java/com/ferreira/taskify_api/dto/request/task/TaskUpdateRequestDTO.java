package com.ferreira.taskify_api.dto.request.task;

import com.ferreira.taskify_api.enums.Priority;

import java.time.LocalDate;

public record TaskUpdateRequestDTO(
        String title,
        String description,
        Priority priority,
        LocalDate dueDate
        ) {
}
