package com.ferreira.taskify_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(name = "ValidationErrorResponse", description = "Resposta para erros de validação de campos")
public record ValidationErrorResponse(
        @Schema(description = "Momento exato do erro (ISO 8601 UTC)", example = "2026-06-22T10:30:00Z")
        Instant timestamp,

        @Schema(description = "Código HTTP do erro", example = "400")
        Integer status,

        @Schema(description = "Título do erro HTTP", example = "Not Found")
        String error,

        @Schema(description = "Lista de erros por campo",
                example = """
                {
                  "title": "Título é obrigatório",
                  "dueDate": "Data deve ser futura"
                }
                """)
        Map<String, String> errors,

        @Schema(description = "Caminho da requisição", example = "/api/tasks/12")
        String path
) {
}
