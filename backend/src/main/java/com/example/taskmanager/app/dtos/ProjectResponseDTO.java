package com.example.taskmanager.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectResponseDTO(
        UUID id,
        String name,
        UUID userId,
        UUID folderId,
        List<TaskTypeResponseDTO> taskTypes,
        List<TaskStatusResponseDTO> taskStatuses,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
