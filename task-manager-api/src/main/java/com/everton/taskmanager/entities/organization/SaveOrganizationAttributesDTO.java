package com.everton.taskmanager.entities.organization;

import com.everton.taskmanager.entities.attributes.CreateAttributeDTO;

import java.util.List;

public record SaveOrganizationAttributesDTO(

        List<CreateAttributeDTO> taskTypes,

        List<CreateAttributeDTO> eventTypes,

        List<CreateAttributeDTO> taskStatuses
) {
}
