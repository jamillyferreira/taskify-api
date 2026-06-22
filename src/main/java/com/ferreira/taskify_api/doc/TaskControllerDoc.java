package com.ferreira.taskify_api.doc;

import com.ferreira.taskify_api.dto.request.task.TaskRequestDTO;
import com.ferreira.taskify_api.dto.request.task.TaskUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.task.TaskResponseDTO;
import com.ferreira.taskify_api.dto.response.task.TaskSummaryResponseDTO;
import com.ferreira.taskify_api.exception.ErrorResponse;
import com.ferreira.taskify_api.exception.ValidationErrorResponse;
import com.ferreira.taskify_api.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Tasks", description = "Gerenciamento de tarefas")
public interface TaskControllerDoc {

    @Operation(
            summary = "Criar nova tarefa",
            description = """
                    Cria uma nova tarefa para o usuário autenticado
                    Regras de negócio:
                    - Título é obrigatório (mínimo 3 caracteres)
                    - Data de vencimento deve ser futura
                    - Prioridade deve ser LOW, MEDIUM ou HIGH
                    - Usuário deve estar autenticado
                    """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarefa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),

            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação - Campos inválidos ou faltando",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),

            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<TaskResponseDTO> createTask(
            @Parameter(description = "Dados da tarefa a ser criada", required = true)
            @RequestBody @Valid TaskRequestDTO request,
            @Parameter(description = "Usuário autenticado", hidden = true) @AuthenticationPrincipal User user);


    @Operation(
            summary = "Busca tarefa por ID",
            description = "Retorna detalhes de uma tarefa específica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa encontrada com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<TaskResponseDTO> findById(
            @Parameter(description = "ID da tarefa", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Usuário autenticado", hidden = true) @AuthenticationPrincipal User user);


    @Operation(
            summary = "Listar todas as tarefas",
            description = "Retorna uma lista com todas as tarefas do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<List<TaskSummaryResponseDTO>> findAll(@AuthenticationPrincipal User user);


    @Operation(
            summary = "Atualizar tarefa",
            description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tarefa atualizada com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação - Campos inválidos ou faltando",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "ID da tarefa", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Usuário autenticado", hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "Dados atualizados da tarefa") @RequestBody TaskUpdateRequestDTO request);


    @Operation(
            summary = "Alternar status da tarefa",
            description = "Atualiza status de uma tarefa específica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status da tarefa atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<Void> toggleCompleted(
            @Parameter(description = "ID da tarefa", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Usuário autenticado", hidden = true) @AuthenticationPrincipal User user);


    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa deletada com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID da tarefa", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Usuário autenticado", hidden = true) @AuthenticationPrincipal User user);

}