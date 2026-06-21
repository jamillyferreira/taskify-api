package com.ferreira.taskify_api.mapper.task;

import com.ferreira.taskify_api.dto.request.task.TaskRequestDTO;
import com.ferreira.taskify_api.dto.response.task.TaskResponseDTO;
import com.ferreira.taskify_api.dto.response.task.TaskSummaryResponseDTO;
import com.ferreira.taskify_api.model.Task;



public interface TaskMapper {
    Task toEntity(TaskRequestDTO request);
    TaskResponseDTO toResponseDTO(Task task);
    TaskSummaryResponseDTO toSummaryResponseDTO(Task task);
}
