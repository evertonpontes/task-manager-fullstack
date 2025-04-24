package com.everton.taskmanager.entities.projects;

import com.everton.taskmanager.entities.attributes.AttributeDTO;

import java.util.List;

public record ProjectAttributesDTO(

        List<AttributeDTO> taskTypes,
        List<AttributeDTO> eventTypes,
        List<AttributeDTO> taskStatuses
) {
}
