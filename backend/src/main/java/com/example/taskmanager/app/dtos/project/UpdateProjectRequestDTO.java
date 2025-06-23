package com.example.taskmanager.app.dtos.project;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequestDTO(
        @NotBlank(message = "folder name is required.")
        String name
) {
}
