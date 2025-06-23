package com.example.taskmanager.app.dtos.attributes;

import jakarta.validation.constraints.NotBlank;

public record SaveTaskTypeRequestDTO(
        @NotBlank(message = "task type name is required.")
        String name,
        String color
) {
}
