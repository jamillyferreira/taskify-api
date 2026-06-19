package com.ferreira.taskify_api.controller;

import com.ferreira.taskify_api.dto.request.LoginRequestDTO;
import com.ferreira.taskify_api.dto.request.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.LoginResponseDTO;
import com.ferreira.taskify_api.dto.response.RegisterResponseDTO;
import com.ferreira.taskify_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        RegisterResponseDTO createdRegister = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegister);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }


}
