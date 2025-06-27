package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.entities.task.TaskPriorityEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record UpdateTaskRequestDTO(
        String title,
        String description,
        TaskPriorityEnum priority,
        LocalDateTime dueDate,
        LocalTime estimatedTime,
        UUID taskTypeId,
        UUID taskStatusId,
        UUID projectId,
        Boolean repeat,
        Boolean completed
) {
}
