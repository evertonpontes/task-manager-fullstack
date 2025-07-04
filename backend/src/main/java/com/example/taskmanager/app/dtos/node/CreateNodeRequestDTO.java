package com.example.taskmanager.app.dtos.node;

import jakarta.validation.constraints.NotBlank;

public record CreateNodeRequestDTO(
        @NotBlank(message = "Name is required.")
        String name
) {
}
