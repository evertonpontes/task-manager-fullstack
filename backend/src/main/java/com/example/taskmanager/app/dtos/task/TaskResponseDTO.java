package com.example.taskmanager.app.dtos.task;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        BigDecimal sortIndex,
        String title,
        String description,
        String priority,
        LocalDateTime dueDate,
        LocalTime estimatedTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID taskTypeId,
        UUID taskStatusId,
        UUID projectId
) {
}
