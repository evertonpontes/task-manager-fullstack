package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;

public record SubTaskResponseDTO(
        String id,
        Double sortIndex,
        String title,
        Instant dueDate,
        LocalTime estimatedTime,
        Boolean completed
) {
}
