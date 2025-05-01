package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;

public record ScheduledWorkResponseDTO(
        String id,
        Double sortIndex,
        Instant date,
        LocalTime estimatedTime,
        String assigneeEmail,
        Boolean completed
) {
}
