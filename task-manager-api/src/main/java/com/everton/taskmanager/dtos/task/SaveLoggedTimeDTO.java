package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;

public record SaveLoggedTimeDTO(
        Instant date,

        LocalTime spentTime,

        String assigneeEmail,

        Boolean billable,

        String comment
) {
}
