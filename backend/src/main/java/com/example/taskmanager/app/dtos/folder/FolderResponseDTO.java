package com.example.taskmanager.app.dtos.folder;

import com.example.taskmanager.app.dtos.project.ProjectResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FolderResponseDTO(
        UUID id,
        BigDecimal sortIndex,
        String name,
        UUID userId,
        List<ProjectResponseDTO> projects,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
