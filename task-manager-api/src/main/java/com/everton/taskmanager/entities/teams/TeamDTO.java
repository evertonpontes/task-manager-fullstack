package com.everton.taskmanager.entities.teams;

import com.everton.taskmanager.entities.attributes.AttributeDTO;

import java.util.List;

public record TeamDTO(

        String id,
        String name,
        List<AttributeDTO> taskTypes,
        List<AttributeDTO> eventTypes,
        List<AttributeDTO> taskStatuses
) {
}
