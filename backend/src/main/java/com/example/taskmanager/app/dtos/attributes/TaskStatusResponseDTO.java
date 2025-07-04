package com.example.taskmanager.app.dtos.attributes;

import com.example.taskmanager.app.entities.TaskStatusKindEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskStatusResponseDTO(
        UUID id,
        Long orderIndex,
        String name,
        TaskStatusKindEnum kind,
        String color,
        Boolean deletable,
        Boolean draggable,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
