package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.dtos.attributes.TaskStatusResponseDTO;
import com.example.taskmanager.app.dtos.attributes.TaskTypeResponseDTO;
import com.example.taskmanager.app.entities.task.TaskPriorityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        BigDecimal commonRank,
        Long statusRank,
        LocalDateTime startAt,
        String title,
        String description,
        TaskPriorityEnum priority,
        LocalDateTime dueDate,
        Integer spentTime,
        Integer estimatedTime,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt,
        List<SubTaskResponseDTO> subTasks,
        UUID userId,
        TaskTypeResponseDTO taskType,
        TaskStatusResponseDTO taskStatus,
        UUID projectId
) {
}
