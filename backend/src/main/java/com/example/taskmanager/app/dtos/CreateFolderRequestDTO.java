package com.example.taskmanager.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateFolderRequestDTO(
        @NotBlank(message = "folder name is required.")
        String name
) {
}
