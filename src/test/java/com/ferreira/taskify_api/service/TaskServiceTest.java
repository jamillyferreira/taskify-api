package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.task.TaskRequestDTO;
import com.ferreira.taskify_api.dto.request.task.TaskUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.task.TaskResponseDTO;
import com.ferreira.taskify_api.enums.Priority;
import com.ferreira.taskify_api.exception.ResourceNotFoundException;
import com.ferreira.taskify_api.exception.TaskAccessDeniedException;
import com.ferreira.taskify_api.mapper.task.TaskMapper;
import com.ferreira.taskify_api.model.Task;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private User authenticatedUser;
    private User otherUser;
    private Task task;
    private TaskRequestDTO requestDTO;
    private TaskResponseDTO responseDTO;
    private TaskUpdateRequestDTO updateRequestDTO;

    @BeforeEach
    void setUp() {
        authenticatedUser = User.builder()
                .id(1L)
                .email("teste@gmail.com")
                .password("teste123")
                .build();

        otherUser = User.builder()
                .id(2L)
                .name("Outro Usuário")
                .email("outro@gmail.com")
                .password("outro123")
                .build();

        task = Task.builder()
                .id(1L)
                .user(authenticatedUser)
                .title("Testando")
                .description("Testando projeto com security e jwt")
                .completed(false)
                .dueDate(LocalDate.now().plusDays(7))
                .priority(Priority.MEDIUM)
                .build();

        requestDTO = new TaskRequestDTO(
                "Testando",
                "Testando projeto com security e jwt",
                Priority.MEDIUM,
                LocalDate.now().plusDays(7)

        );
        responseDTO = new TaskResponseDTO(
                1L,
                authenticatedUser.getName(),
                "Testando",
                "Testando projeto com security e jwt",
                false,
                LocalDate.now().plusDays(7),
                Priority.MEDIUM,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        updateRequestDTO = new TaskUpdateRequestDTO(
                "Título Atualizado",
                "Descrição atualizada",
                Priority.HIGH,
                LocalDate.now().plusDays(14)
        );
    }

    @Test
    @DisplayName("Deve criar tarefa com sucesso")
    void shouldCreateTaskWithSuccess() {

        when(taskMapper.toEntity(requestDTO)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toResponseDTO(any(Task.class))).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.createTask(requestDTO, authenticatedUser);

        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        assertEquals(responseDTO.title(), result.title());
        assertEquals(responseDTO.description(), result.description());
        assertFalse(result.completed());

        verify(taskMapper).toEntity(requestDTO);
        verify(taskMapper).toResponseDTO(any(Task.class));
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Deve lançar excecao ao criar tarefa com dados invalidos")
    void shouldCThrowExceptionWhenCreatingTaskWithInvalidData() {
        when(taskMapper.toEntity(any(TaskRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(requestDTO, authenticatedUser));
    }

    @Test
    @DisplayName("Deve buscar tarefa por ID com sucesso")
    void shouldFindTaskByIdWithSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.findById(1L, authenticatedUser);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Testando", result.title());

        verify(taskRepository).findById(1L);
        verify(taskMapper).toResponseDTO(task);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar tarefa inexistente")
    void shouldThrowResourceNotFoundExceptionWhenTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                taskService.findById(999L, authenticatedUser));

        verify(taskRepository).findById(999L);
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("Deve lançar TaskAccessDeniedException ao buscar tarefa de outro usuário")
    void shouldThrowTaskAccessDeniedExceptionWhenFindingOtherUsersTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(TaskAccessDeniedException.class, () ->
                taskService.findById(1L, otherUser));

        verify(taskRepository).findById(1L);
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("Deve listar todas as tarefas do usuário com sucesso")
    void shouldFindAllTasksByUserWithSuccess() {
        List<Task> tasks = List.of(task);
        when(taskRepository.findAllByUser(authenticatedUser)).thenReturn(tasks);
        when(taskMapper.toResponseDTO(any(Task.class))).thenReturn(responseDTO);

        List<TaskResponseDTO> result = taskService.findAll(authenticatedUser);

        assertNotNull(result);
        assertEquals(1L, result.size());
        assertEquals(responseDTO.id(), result.getFirst().id());

        verify(taskRepository).findAllByUser(authenticatedUser);
        verify(taskMapper, times(tasks.size())).toResponseDTO(any(Task.class));
    }


    @Test
    @DisplayName("Deve atualizar tarefa com sucesso")
    void shouldUpdateTaskWithSuccess() {
        Task updatedTask = Task.builder()
                .id(1L)
                .user(authenticatedUser)
                .title("Título Atualizado")
                .description("Descrição atualizada")
                .completed(false)
                .dueDate(LocalDate.now().plusDays(14))
                .priority(Priority.HIGH)
                .build();

        TaskResponseDTO updatedResponseDTO = new TaskResponseDTO(
                1L,
                authenticatedUser.getName(),
                "Título Atualizado",
                "Descrição atualizada",
                false,
                LocalDate.now().plusDays(14),
                Priority.HIGH,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toResponseDTO(any(Task.class))).thenReturn(updatedResponseDTO);

        TaskResponseDTO result = taskService.updateTask(1L, authenticatedUser, updateRequestDTO);

        assertNotNull(result);
        assertEquals("Título Atualizado", result.title());
        assertEquals("Descrição atualizada", result.description());
        assertEquals(Priority.HIGH, result.priority());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toResponseDTO(any(Task.class));
    }

    @Test
    @DisplayName("Deve definir prioridade MEDIUM quando não informada")
    void shouldSetMediumPriorityWhenPriorityIsNull() {
        TaskRequestDTO requestWithoutPriority = new TaskRequestDTO(
                "Test task",
                "Description",
                null,
                LocalDate.now().plusDays(7)
        );

        Task taskWithoutPriority = Task.builder()
                .id(1L)
                .user(authenticatedUser)
                .title("Test task")
                .description("Description")
                .priority(null)
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        when(taskMapper.toEntity(requestWithoutPriority)).thenReturn(taskWithoutPriority);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setPriority(Priority.MEDIUM);
            return savedTask;
        });

        when(taskMapper.toResponseDTO(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            return new TaskResponseDTO(
                    task.getId(),
                    authenticatedUser.getName(),
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted(),
                    task.getDueDate(),
                    task.getPriority(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        });

        TaskResponseDTO result = taskService.createTask(requestWithoutPriority, authenticatedUser);
        assertEquals(Priority.MEDIUM, result.priority());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar tarefa de outro usuário")
    void shouldThrowExceptionWhenUpdatingOtherUsersTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(TaskAccessDeniedException.class, () ->
                taskService.updateTask(1L, otherUser, updateRequestDTO));

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Deve alternar status da tarefa")
    void shouldToggleTaskCompletion() {
        Task pendingTask = Task.builder()
                .id(1L)
                .user(authenticatedUser)
                .title("Test task")
                .completed(false)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(pendingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(pendingTask);

        taskService.toggleComplete(1L, authenticatedUser);

        assertTrue(pendingTask.isCompleted());
        verify(taskRepository).save(pendingTask);
    }

    @Test
    @DisplayName("Deve deletar tarefa com sucesso")
    void shouldDeleteTaskWithSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L, authenticatedUser);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }




}