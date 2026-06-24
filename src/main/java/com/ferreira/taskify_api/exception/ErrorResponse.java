package com.ferreira.taskify_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "ErrorResponse", description = "Resposta padrão para erros da aplicação")
public record ErrorResponse(
        @Schema(description = "Momento exato do erro (ISO 8601 UTC)", example = "2026-06-22T10:30:00Z")
        Instant timestamp,

        @Schema(description = "Código HTTP do erro", example = "404")
        Integer status,

        @Schema(description = "Título do erro HTTP", example = "Not Found")
        String error,

        @Schema(description = "Mensagem detalhada do erro", example = "Tarefa não encontrada com ID: 12")
        String message,

        @Schema(description = "Caminho da requisição", example = "/api/tasks/12")
        String path
) {
}
