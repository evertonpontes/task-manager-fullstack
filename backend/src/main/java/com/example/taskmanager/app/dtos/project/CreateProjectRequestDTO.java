package com.example.taskmanager.app.dtos.project;

import com.example.taskmanager.app.dtos.attributes.SaveTaskStatusRequestDTO;
import com.example.taskmanager.app.dtos.attributes.SaveTaskTypeRequestDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateProjectRequestDTO(
        @NotBlank(message = "folder name is required.")
        String name,
        List<SaveTaskTypeRequestDTO> taskTypes,
        List<SaveTaskStatusRequestDTO> taskStatuses
) {
}
