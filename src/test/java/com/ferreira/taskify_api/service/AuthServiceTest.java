package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.auth.LoginRequestDTO;
import com.ferreira.taskify_api.dto.request.auth.RegisterRequestDTO;
import com.ferreira.taskify_api.dto.response.auth.LoginResponseDTO;
import com.ferreira.taskify_api.dto.response.auth.RegisterResponseDTO;
import com.ferreira.taskify_api.exception.BusinessException;
import com.ferreira.taskify_api.mapper.user.UserMapper;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.UserRepository;
import com.ferreira.taskify_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequestDTO;
    private RegisterResponseDTO registerResponseDTO;
    private LoginRequestDTO loginRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO(
                "Ana Clara",
                "anaclara@example.com",
                "password123"
        );

        registerResponseDTO = new RegisterResponseDTO(
                1L,
                "Ana Clara",
                "anaclara@example.com"
        );

        user = User.builder()
                .id(1L)
                .name("Ana Clara")
                .email("anaclara@example.com")
                .password("encodedPassword")
                .build();

        loginRequestDTO = new LoginRequestDTO(
                "anaclara@example.com",
                "password123"
        );
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void register_WhenEmailIsAvailable_ShouldRegisterUser() {

        when(userRepository.existsByEmail(registerRequestDTO.email())).thenReturn(false);
        when(userMapper.toEntity(registerRequestDTO)).thenReturn(user);
        when(passwordEncoder.encode(registerRequestDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toRegisterResponseDTO(user)).thenReturn(registerResponseDTO);

        RegisterResponseDTO result = authService.register(registerRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("anaclara@example.com");
        assertThat(result.name()).isEqualTo("Ana Clara");

        verify(userRepository).existsByEmail(registerRequestDTO.email());
        verify(userMapper).toEntity(registerRequestDTO);
        verify(passwordEncoder).encode(registerRequestDTO.password());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toRegisterResponseDTO(user);
    }

    @Test
    @DisplayName("Deve lançar exceção BusinessException se email já existir")
    void register_WhenEmailAlreadyExists_ShouldThrowBusinessException() {
        when(userRepository.existsByEmail(registerRequestDTO.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("E-mail já cadastrado");

        verify(userRepository).existsByEmail(registerRequestDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token")
    void login_WhenCredentialsAreValid_ShouldReturnToken() {
        String token = "test-jwt-token-123";

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(token);

        LoginResponseDTO result = authService.login(loginRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("test-jwt-token-123");
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    @DisplayName("Deve lançar exceção quando credenciais forem inválidas")
    void login_WhenCredentialsAreInvalid_ShouldThrowException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThatThrownBy(() -> authService.login(loginRequestDTO))
                .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).generateToken(any());
    }

}