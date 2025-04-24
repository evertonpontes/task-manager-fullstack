package com.everton.taskmanager.entities.teams;

import com.everton.taskmanager.entities.attributes.CreateAttributeDTO;

import java.util.List;

public record SaveTeamAttributesDTO(

        List<CreateAttributeDTO> taskTypes,

        List<CreateAttributeDTO> eventTypes,

        List<CreateAttributeDTO> taskStatuses
) {
}
