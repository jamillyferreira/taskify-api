package com.ferreira.taskify_api.controller;

import com.ferreira.taskify_api.doc.TaskControllerDoc;
import com.ferreira.taskify_api.dto.request.task.TaskRequestDTO;
import com.ferreira.taskify_api.dto.request.task.TaskUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.task.TaskResponseDTO;
import com.ferreira.taskify_api.dto.response.task.TaskSummaryResponseDTO;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController implements TaskControllerDoc {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestBody @Valid TaskRequestDTO request,
            @AuthenticationPrincipal User user) {
        TaskResponseDTO response = taskService.createTask(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        TaskResponseDTO response = taskService.findById(id, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskSummaryResponseDTO>> findAll(@AuthenticationPrincipal User user) {
        List<TaskSummaryResponseDTO> response = taskService.findAll(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody TaskUpdateRequestDTO request) {
        TaskResponseDTO response = taskService.updateTask(id, user, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-complete")
    public ResponseEntity<Void> toggleCompleted(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.toggleComplete(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
