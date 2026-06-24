package com.ferreira.taskify_api.doc;

import com.ferreira.taskify_api.dto.request.user.ProfileUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.exception.ErrorResponse;
import com.ferreira.taskify_api.exception.ValidationErrorResponse;
import com.ferreira.taskify_api.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Profile", description = "Gerenciamento do perfil do usuário autenticado")
public interface ProfileControllerDoc {

    @Operation(
            summary = "Buscar perfil",
            description = "Retorna os dados do perfil do usuário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal User user);


    @Operation(
            summary = "Atualizar perfil",
            description = "Atualiza parcialmente os dados do usuário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil atualizado sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação - Campos inválidos",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ProfileUpdateRequestDTO request);


    @Operation(
            summary = "Deletar perfil",
            description = "Deleta conta do usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Perfil deletado sucesso"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado - Token JWT inválido, expirado ou ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteMyProfile(@AuthenticationPrincipal User user);
}
