package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;

public record SaveScheduledWorksDTO(
        Instant date,
        LocalTime estimatedTime,
        String assigneeEmail,
        Boolean completed
) {
}
