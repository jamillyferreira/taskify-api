package com.ferreira.taskify_api.mapper.task;

import com.ferreira.taskify_api.dto.request.TaskRequestDTO;
import com.ferreira.taskify_api.dto.response.TaskResponseDTO;
import com.ferreira.taskify_api.model.Task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());
        return task;
    }

    @Override
    public TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getUser().getName(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getDueDate(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
