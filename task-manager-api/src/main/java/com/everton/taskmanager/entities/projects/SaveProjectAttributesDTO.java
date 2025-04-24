package com.everton.taskmanager.entities.projects;

import com.everton.taskmanager.entities.attributes.CreateAttributeDTO;

import java.util.List;

public record SaveProjectAttributesDTO(

        List<CreateAttributeDTO> taskTypes,

        List<CreateAttributeDTO> eventTypes,

        List<CreateAttributeDTO> taskStatuses
) {
}
