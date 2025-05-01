package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;

public record LoggedTimeResponseDTO(
        String id,
        Double sortIndex,
        Instant date,
        LocalTime spentTime,
        String assigneeEmail,
        Boolean billable,
        String comment
) {
}
