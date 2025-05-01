package com.everton.taskmanager.dtos.task;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

public record TaskResponseDTO(
        String id,
        Double sortIndex,
        String title,
        Instant dueDate,
        LocalTime estimatedTime,
        String description,
        String status,
        String type,
        String priority,
        List<String> attachedFiles,
        Boolean repeatTask,
        List<SubTaskResponseDTO> subTasks,
        List<ScheduledWorkResponseDTO> scheduledWorks,
        List<LoggedTimeResponseDTO> loggedTimes
) {
}
