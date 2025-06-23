package com.example.taskmanager.app.dtos.attributes;

import jakarta.validation.constraints.NotBlank;

public record SaveTaskStatusRequestDTO(
        @NotBlank(message = "task status name is required.")
        String name,
        String color,
        Boolean isTaskCompleted
) {
}
