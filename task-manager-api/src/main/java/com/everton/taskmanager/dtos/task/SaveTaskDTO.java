package com.everton.taskmanager.dtos.task;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

public record SaveTaskDTO(
        @NotBlank
        String title,

        Instant dueDate,

        LocalTime estimatedTime,

        String assigneeEmail,

        String description,

        String status,

        String type,
        
        String priority,

        List<String> attachedFiles,

        Boolean repeatTask
) {
}
