package com.ferreira.taskify_api.dto.response.task;

import com.ferreira.taskify_api.enums.Priority;

import java.time.LocalDate;

public record TaskSummaryResponseDTO(
        Long id,
        String ownerName,
        String title,
        String description,
        boolean completed,
        LocalDate dueDate,
        Priority priority
) {
}
