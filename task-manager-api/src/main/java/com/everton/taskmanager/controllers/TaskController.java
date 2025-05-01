package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.task.*;
import com.everton.taskmanager.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations/{organizationId}/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable("organizationId") String organizationId,
                                                      @RequestBody @Valid SaveTaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(organizationId, taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable("taskId") String taskId) {
        return  new ResponseEntity<>(taskService.findTaskById(taskId), HttpStatus.OK);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("taskId") String taskId,
                                                       @RequestBody @Valid SaveTaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.updateTask(taskId, taskDTO), HttpStatus.OK);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
