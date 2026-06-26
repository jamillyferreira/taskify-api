package com.ferreira.taskify_api.dto.request.task;

import com.ferreira.taskify_api.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

@Schema(name = "TaskRequestDTO", description = "Dados para criação de uma nova tarefa")
public record TaskRequestDTO(
        @Size(min = 3, message = "Título deve ter no mínimo 3 caracteres")
        @NotBlank(message = "Título é obrigatório")
        String title,

        String description,

        @Schema(description = "Prioridade")
        Priority priority,

        @Schema(description = "Data limite", example = "2026-06-22")
        @FutureOrPresent(message = "Data de vencimento não pode ser no passado")
        LocalDate dueDate
) {
}
