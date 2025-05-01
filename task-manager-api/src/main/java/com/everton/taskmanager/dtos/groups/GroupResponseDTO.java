package com.everton.taskmanager.dtos.groups;

public record GroupResponseDTO(
        String id,
        Double sortIndex,
        String name
) {
}
