package com.example.taskmanager.app.dtos.task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateTaskRequestDTO(
        String title,
        String description,
        String priority,
        LocalDateTime dueDate,
        LocalTime estimatedTime,
        UUID taskTypeId,
        UUID taskStatusId,
        UUID projectId,
        Boolean repeat,
        Boolean completed,
        List<CreateSubTaskRequestDTO> subTasks,
        List<String> attachments
) {
    public CreateTaskRequestDTO {
        if (subTasks == null) {
            subTasks = List.of();
        }
    }
}
