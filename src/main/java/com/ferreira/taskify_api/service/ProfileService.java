package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.user.ProfileUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.exception.BusinessException;
import com.ferreira.taskify_api.mapper.user.UserMapper;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponseDTO getMyProfile(User authenticatedUser) {
        log.debug("Buscando perfil do usuário - ID: {}", authenticatedUser.getId());

        return userMapper.toUserResponseDTO(authenticatedUser);
    }

    public UserResponseDTO updateMyProfile(User authenticatedUser, ProfileUpdateRequestDTO request) {
        log.debug("Atualizando perfil do usuário - ID: {}", authenticatedUser.getId());

        if (hasValue(request.name())) authenticatedUser.setName(request.name());

        if (hasValue(request.email())) {
            if (!request.email().equals(authenticatedUser.getEmail())) {
                if (userRepository.existsByEmail(request.email())) {
                    throw new BusinessException("E-mail já está em uso");
                }
                authenticatedUser.setEmail(request.email());
            }
        }

        User updatedUser = userRepository.save(authenticatedUser);
        log.info("Perfil atualizado com sucesso - ID: {}", authenticatedUser.getId());

        return userMapper.toUserResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteMyAccount(User authenticatedUser) {
        log.debug("Deletando conta do usuário - ID: {}", authenticatedUser.getId());

        userRepository.delete(authenticatedUser);

        log.info("Conta deletada com sucesso - ID: {}", authenticatedUser.getId());
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
