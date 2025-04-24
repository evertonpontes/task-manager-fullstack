package com.everton.taskmanager.entities.organization;

import com.everton.taskmanager.entities.attributes.AttributeDTO;

import java.util.List;

public record OrganizationAttributesDTO(
        List<AttributeDTO> taskTypes,
        List<AttributeDTO> eventTypes,
        List<AttributeDTO> taskStatuses
) {
}
