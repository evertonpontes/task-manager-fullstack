package com.everton.taskmanager.entities.teams;

import com.everton.taskmanager.entities.attributes.AttributeDTO;

import java.util.List;

public record TeamAttributesDTO(

        List<AttributeDTO> taskTypes,
        List<AttributeDTO> eventTypes,
        List<AttributeDTO> taskStatuses
) {
}
