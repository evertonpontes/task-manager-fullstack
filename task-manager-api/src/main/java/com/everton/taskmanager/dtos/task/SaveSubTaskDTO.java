package com.everton.taskmanager.dtos.task;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalTime;

public record SaveSubTaskDTO(
        @NotBlank
        String title,

        Instant dueDate,

        LocalTime estimatedTime,

        String assigneeEmail,

        Boolean completed
) {
}
