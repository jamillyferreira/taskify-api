package com.ferreira.taskify_api.doc;

import com.ferreira.taskify_api.dto.request.auth.LoginRequestDTO;
import com.ferreira.taskify_api.dto.request.auth.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.auth.LoginResponseDTO;
import com.ferreira.taskify_api.dto.response.auth.RegisterResponseDTO;
import com.ferreira.taskify_api.exception.ErrorResponse;
import com.ferreira.taskify_api.exception.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Gerenciamento de Login e Registro")
public interface AuthControllerDoc {

    @Operation(
            summary = "Criar uma nova conta",
            description = "Cria uma nova conta de usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Novo registro criado com sucesso",
                    content = @Content(schema = @Schema(implementation = RegisterResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação - Campos inválidos ou faltando",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - E-mail já cadastrado no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<RegisterResponseDTO> register(
            @Parameter(description = "Dados do usuário para criar registro", required = true)
            @RequestBody @Valid RegisterRequestDTO request);


    @Operation(
            summary = "Fazer login",
            description = "Realiza autenticação do usuário e retorna token JWT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação - Campos inválidos ou faltando",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Credenciais inválidas (usuário ou senha incorretos)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<LoginResponseDTO> login(
            @Parameter(description = "Credenciais de acesso do usuário", required = true)
            @RequestBody @Valid LoginRequestDTO request);
}
