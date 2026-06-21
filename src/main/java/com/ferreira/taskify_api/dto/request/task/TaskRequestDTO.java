package com.ferreira.taskify_api.dto.request.task;

import com.ferreira.taskify_api.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        String title,

        String description,
        Priority priority,

        @FutureOrPresent(message = "Data de vencimento não pode ser no passado")
        LocalDate dueDate
) {
}
