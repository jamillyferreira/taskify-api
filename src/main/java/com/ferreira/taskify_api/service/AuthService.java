package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.LoginRequestDTO;
import com.ferreira.taskify_api.dto.request.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.LoginResponseDTO;
import com.ferreira.taskify_api.dto.response.RegisterResponseDTO;
import com.ferreira.taskify_api.exception.BusinessException;
import com.ferreira.taskify_api.mapper.user.UserMapper;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.UserRepository;
import com.ferreira.taskify_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        log.info("Tentativa de cadastro de novo usuário");

        if (userRepository.existsByEmail(request.email())) {
            log.error("Tentativa de cadastro com e-mail já existente");
            throw new BusinessException("E-mail já cadastrado");
        }

        User user = userMapper.toEntity(request);
        String encodedPassword = passwordEncoder.encode(request.password());
        user.updatePassword(encodedPassword);

        User savedUser = userRepository.save(user);
        log.info("Registro criado com sucesso - ID: {}", savedUser.getId());
        return userMapper.toRegisterResponseDTO(savedUser);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Tentativa de login para o e-mail: {}", request.email());

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        log.info("Login realizado com sucesso para o e-mail: {}", userDetails.getUsername());
        return new LoginResponseDTO(token);
    }
}
