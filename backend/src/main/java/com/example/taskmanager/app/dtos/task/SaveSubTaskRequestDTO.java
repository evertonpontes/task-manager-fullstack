package com.example.taskmanager.app.dtos.task;

import com.example.taskmanager.app.entities.task.StatusEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record SaveSubTaskRequestDTO(
        String title,
        LocalDateTime dueDate,
        StatusEnum status,
        Integer estimatedTime
) {
}
