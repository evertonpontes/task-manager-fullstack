package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.entities.task.TaskPriorityEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateTaskRequestDTO(
        String title,
        String description,
        TaskPriorityEnum priority,
        LocalDateTime startAt,
        LocalDateTime dueDate,
        Integer spentTime,
        Integer estimatedTime,
        UUID taskTypeId,
        UUID taskStatusId,
        List<SaveSubTaskRequestDTO> subTasks,
        List<String> attachments
) {
    public CreateTaskRequestDTO {
        if (subTasks == null) {
            subTasks = List.of();
        }
    }
}
