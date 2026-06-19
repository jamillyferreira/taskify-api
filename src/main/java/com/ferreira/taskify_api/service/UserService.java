package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.response.UserResponseDTO;
import com.ferreira.taskify_api.exception.ResourceNotFoundException;
import com.ferreira.taskify_api.mapper.user.UserMapper;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO findById(Long id) {
        log.info("Buscando usuário com o ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
                });

        log.info("Usuário retornado - ID: {} | Nome: {}", user.getId(), user.getName());
        return userMapper.toUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAll() {
        log.info("Busca todos os usuários");
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    public void delete(Long id) {
        log.info("Busca usuario com o ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
                });

        userRepository.delete(user);
        log.info("Usuário removido com o ID: {}", id);
    }
}
