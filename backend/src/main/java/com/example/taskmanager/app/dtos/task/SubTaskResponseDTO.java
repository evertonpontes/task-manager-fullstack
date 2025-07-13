package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.entities.task.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record SubTaskResponseDTO(
        UUID id,
        BigDecimal rank,
        String title,
        LocalDateTime dueDate,
        StatusEnum status,
        Integer estimatedTime,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt,
        UUID parentTaskId
) {
}
