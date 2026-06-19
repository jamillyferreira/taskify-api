package com.ferreira.taskify_api.service;

import com.ferreira.taskify_api.dto.request.TaskRequestDTO;
import com.ferreira.taskify_api.dto.request.TaskUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.TaskResponseDTO;
import com.ferreira.taskify_api.enums.Priority;
import com.ferreira.taskify_api.exception.ResourceNotFoundException;
import com.ferreira.taskify_api.exception.TaskAccessDeniedException;
import com.ferreira.taskify_api.mapper.task.TaskMapper;
import com.ferreira.taskify_api.model.Task;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(TaskRequestDTO request, User authenticatedUser) {
        log.debug("Criando tarefa para usuário {}", authenticatedUser.getId());

        Task task = taskMapper.toEntity(request);
        if (task.getPriority() == null) task.setPriority(Priority.MEDIUM);
        task.setUser(authenticatedUser);

        Task taskCreated = taskRepository.save(task);
        log.info("Tarefa criada com sucesso - taskId: {}, userId: {}", taskCreated.getId(), authenticatedUser.getId());
        return taskMapper.toResponseDTO(taskCreated);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id, User authenticatedUser) {
        log.debug("Buscando tarefa {} para usuário {}", id, authenticatedUser.getId());

        Task task = findTaskAndValidateOwnership(id, authenticatedUser);

        log.debug("Tarefa {} encontrada para usuário {}", task.getId(), authenticatedUser.getId());
        return taskMapper.toResponseDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAll(User authenticatedUser) {
        log.debug("Listando tarefas do usuário {}", authenticatedUser.getId());

        List<Task> tasks = taskRepository.findAllByUser(authenticatedUser);

        log.info("Listagem de tarefas realizada - userId: {}, totalTasks: {}", authenticatedUser.getId(), tasks.size());
        return tasks
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO updateTask(Long id, User authenticatedUser, TaskUpdateRequestDTO request) {
        log.debug("Atualizando parcialmente tarefa {} do usuário {}", id, authenticatedUser.getId());

        Task task = findTaskAndValidateOwnership(id, authenticatedUser);

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.priority() != null) task.setPriority(request.priority());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());

        Task updatedTask = taskRepository.save(task);

        log.info("Tarefa atualizada com sucesso - taskId: {}, userId: {}", task.getId(), authenticatedUser.getId());
        return taskMapper.toResponseDTO(updatedTask);
    }

    public void toggleComplete(Long id, User authenticatedUser) {
        Task task = findTaskAndValidateOwnership(id, authenticatedUser);
        task.toggleCompleted();
        taskRepository.save(task);
    }

    public void deleteTask(Long id, User authenticatedUser) {
        log.debug("Removendo tarefa {} do usuário {}", id, authenticatedUser.getId());

        Task task = findTaskAndValidateOwnership(id, authenticatedUser);

        log.info("Tarefa removida com sucesso - taskId: {}, userId: {}", id, authenticatedUser.getId());
        taskRepository.delete(task);
    }

    private Task findTaskAndValidateOwnership(Long id, User authenticatedUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tarefa não encontrada - taskId: {}", id);
                    return new ResourceNotFoundException("Tarefa não encontrada");
                });

        if (!task.getUser().getId().equals(authenticatedUser.getId())) {
            throw new TaskAccessDeniedException("Usuário não tem acesso a esta tarefa");
        }

        return task;
    }
}
