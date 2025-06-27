package com.example.taskmanager.app.dtos.task;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record SubTaskResponseDTO(
        UUID id,
        BigDecimal sortIndex,
        String title,
        LocalDateTime dueDate,
        LocalTime estimatedTime,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt,
        UUID parentTaskId
) {
}
