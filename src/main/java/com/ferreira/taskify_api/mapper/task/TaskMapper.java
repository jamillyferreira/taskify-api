package com.ferreira.taskify_api.mapper.task;

import com.ferreira.taskify_api.dto.request.TaskRequestDTO;
import com.ferreira.taskify_api.dto.response.TaskResponseDTO;
import com.ferreira.taskify_api.model.Task;



public interface TaskMapper {
    Task toEntity(TaskRequestDTO request);
    TaskResponseDTO toResponseDTO(Task task);
}
