package com.example.taskmanager.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskStatusRequestDTO(
        @NotBlank(message = "task status name is required.")
        String name,
        String color,
        Boolean isTaskCompleted
) {
}
