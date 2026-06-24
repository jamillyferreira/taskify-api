package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.user.ProfileUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.exception.BusinessException;
import com.ferreira.taskify_api.mapper.user.UserMapper;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProfileService profileService;

    private User authenticatedUser;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        authenticatedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .build();

        userResponseDTO = new UserResponseDTO(
                1L,
                "Test User",
                "test@example.com",
                null,
                null
        );
    }

    @Test
    @DisplayName("Deve buscar perfil do usuário autenticado com sucesso")
    void getMyProfile_WhenUserIsValid_ShouldReturnUserResponseDTO() {

        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = profileService.getMyProfile(authenticatedUser);

        assertNotNull(result);
        assertEquals(authenticatedUser.getId(), result.id());
        assertEquals(authenticatedUser.getName(), result.name());
        assertEquals(authenticatedUser.getEmail(), result.email());

        verify(userMapper, times(1)).toUserResponseDTO(authenticatedUser);
        verifyNoMoreInteractions(userMapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Deve retornar null quando mapper retornar null")
    void getMyProfile_WhenMapperReturnsNull_ShouldReturnNull() {
        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(null);

        UserResponseDTO result = profileService.getMyProfile(authenticatedUser);

        assertNull(result);
        verify(userMapper).toUserResponseDTO(authenticatedUser);
    }

    @Test
    @DisplayName("Deve atualizar nome quando fornecido")
    void updateMyProfile_WhenNameIsProvided_ShouldUpdateName() {

        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
                "New Name", null);

        User savedUser = User.builder()
                .id(1L)
                .name("New Name")
                .email("test@example.com")
                .build();

        UserResponseDTO expectedUser = new UserResponseDTO(
                1L, "New Name", "test@example.com", null, null);

        when(userRepository.save(authenticatedUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(expectedUser);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("New Name", result.name());

         verify(userRepository).save(authenticatedUser);
         verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    @DisplayName("Deve atualizar email quando disponível")
    void updateMyProfile_WhenEmailIsAvailable_ShouldUpdateEmail() {
        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
                null, "new@example.com");

        User savedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("new@example.com")
                .build();

        UserResponseDTO expectedUser = new UserResponseDTO(
                1L, "Test User", "new@example.com",  null, null
        );

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(authenticatedUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(expectedUser);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("new@example.com", result.email());

        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(authenticatedUser);
    }

    @Test
    @DisplayName("Deve lançar exceção BusinessException quando email já está em uso")
    void updateMyProfile_WhenEmailIsAlreadyInUse_ShouldThrowBusinessException() {
        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
          null, "inuse@example.com"
        );

        when(userRepository.existsByEmail("inuse@example.com")).thenReturn(true);

        assertThrows(BusinessException.class,  () ->
                profileService.updateMyProfile(authenticatedUser, request));

        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUserResponseDTO(any());
    }

    @Test
    @DisplayName("Não deve atualizar nome quando nulo")
    void updateMyProfile_WhenNameIsNull_ShouldNotUpdateName() {
        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(null, null);
        when(userRepository.save(authenticatedUser)).thenReturn(authenticatedUser);
        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("Test User", result.name());
    }

    @Test
    @DisplayName("Deve atualizar nome e email quando ambos fornecidos")
    void updateMyProfile_WhenBothFieldsProvided_ShouldUpdateBoth() {
        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
                "New Name", "new@example.com");

        User savedUser = User.builder()
                .id(1L).name("New Name").email("new@example.com").build();

        UserResponseDTO expectedDTO = new UserResponseDTO(
                1L, "New Name", "new@example.com", null, null);

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(authenticatedUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(expectedDTO);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("New Name", result.name());
        assertEquals("new@example.com", result.email());

        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(authenticatedUser);
    }


    @Test
    @DisplayName("Não deve atualizar nome quando estiver em branco")
    void updateMyProfile_WhenNameIsBlank_ShouldNotUpdateName() {

        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(" ", null);

        when(userRepository.save(authenticatedUser)).thenReturn(authenticatedUser);
        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("Test User", result.name());

        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    @DisplayName("Não deve atualizar email quando estiver em branco")
    void updateMyProfile_WhenEmailIsBlank_ShouldNotUpdateEmail() {

        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(null, " ");
        when(userRepository.save(authenticatedUser)).thenReturn(authenticatedUser);
        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = profileService.updateMyProfile(authenticatedUser, request);

        assertEquals("test@example.com", result.email());
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    @DisplayName("Não deve verificar disponibilidade quando email for igual ao atual")
    void updateMyProfile_WhenEmailIsSameAsCurrent_ShouldNotCheckRepository() {

        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
                null, "test@example.com"
        );
        when(userRepository.save(authenticatedUser)).thenReturn(authenticatedUser);
        when(userMapper.toUserResponseDTO(authenticatedUser)).thenReturn(userResponseDTO);

        profileService.updateMyProfile(authenticatedUser, request);

        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository).save(authenticatedUser);
    }

    @Test
    @DisplayName("Deve deletar conta do usuário autenticado")
    void deleteMyAccount_ShouldDeleteAuthenticatedUser() {
        profileService.deleteMyAccount(authenticatedUser);

        verify(userRepository).delete(authenticatedUser);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}