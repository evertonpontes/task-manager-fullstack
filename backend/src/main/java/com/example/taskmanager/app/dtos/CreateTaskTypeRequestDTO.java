package com.example.taskmanager.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskTypeRequestDTO(
        @NotBlank(message = "task type name is required.")
        String name,
        String color
) {
}
