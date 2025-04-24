package com.everton.taskmanager.entities.projects;

import jakarta.validation.constraints.NotBlank;

public record SaveProjectDTO(

        @NotBlank
        String name
) {
}
