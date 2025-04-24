package com.everton.taskmanager.entities.projects;

import com.everton.taskmanager.entities.attributes.CreateAttributeDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateProjectDTO(

        @NotBlank
        String name,

        List<CreateAttributeDTO> taskTypes,

        List<CreateAttributeDTO> eventTypes,

        List<CreateAttributeDTO> taskStatuses
) {
}
