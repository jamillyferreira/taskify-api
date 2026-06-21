package com.ferreira.taskify_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequestDTO(
        @Size(min =  3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String name,

        @Email(message = "E-mail deve ser válido")
        String email
) {
}
