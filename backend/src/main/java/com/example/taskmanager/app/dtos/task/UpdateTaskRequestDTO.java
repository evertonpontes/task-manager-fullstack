package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.entities.task.TaskPriorityEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record UpdateTaskRequestDTO(
        BigDecimal commonRank,
        Long statusRank,
        String title,
        String description,
        TaskPriorityEnum priority,
        LocalDateTime startAt,
        LocalDateTime dueDate,
        Integer spentTime,
        Integer estimatedTime,
        UUID taskTypeId,
        UUID taskStatusId,
        UUID projectId
) {
}
