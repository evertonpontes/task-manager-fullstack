package com.example.taskmanager.app.controllers;

import com.example.taskmanager.app.dtos.task.CreateTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.SaveSubTaskRequestDTO;
import com.example.taskmanager.app.dtos.task.TaskResponseDTO;
import com.example.taskmanager.app.dtos.task.UpdateTaskRequestDTO;
import com.example.taskmanager.app.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(@PathVariable UUID projectId, @Valid @RequestBody CreateTaskRequestDTO request) {
        return taskService.createTask(projectId, request);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasksForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{taskId}/subtasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO addSubTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody SaveSubTaskRequestDTO request) {
        return taskService.createSubTask(taskId, request);
    }

    @PutMapping("/{taskId}/subtasks/{subTaskId}")
    public ResponseEntity<TaskResponseDTO> updateSubTask(
            @PathVariable UUID taskId,
            @PathVariable UUID subTaskId,
            @Valid @RequestBody SaveSubTaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateSubTask(taskId, subTaskId, request));
    }

    @DeleteMapping("/{taskId}/subtasks/{subTaskId}")
    public ResponseEntity<Void> deleteSubTask(
            @PathVariable UUID taskId,
            @PathVariable UUID subTaskId) {
        taskService.deleteSubTask(taskId, subTaskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
