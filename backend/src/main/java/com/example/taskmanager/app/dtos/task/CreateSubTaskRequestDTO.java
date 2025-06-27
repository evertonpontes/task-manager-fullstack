package com.example.taskmanager.app.dtos.task;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateSubTaskRequestDTO(
        String title,
        LocalDateTime dueDate,
        LocalTime estimatedTime
) {
}
