package com.example.taskmanager.app.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateProjectRequestDTO(
        @NotBlank(message = "folder name is required.")
        String name,
        List<CreateTaskTypeRequestDTO> taskTypes,
        List<CreateTaskStatusRequestDTO> taskStatuses
) {
}
