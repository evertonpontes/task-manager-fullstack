package com.everton.taskmanager.entities.projects;

import jakarta.validation.constraints.NotBlank;

public record SaveFolderDTO(

        @NotBlank
        String name
) {
}
