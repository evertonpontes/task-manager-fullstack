package com.example.taskmanager.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskTypeResponseDTO(
        UUID id,
        BigDecimal sortOrder,
        String name,
        String color,
        UUID projectId,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
