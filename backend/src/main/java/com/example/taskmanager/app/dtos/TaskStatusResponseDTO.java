package com.example.taskmanager.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskStatusResponseDTO(
        UUID id,
        BigDecimal sortIndex,
        String name,
        String color,
        Boolean isTaskCompleted,
        UUID projectId,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
