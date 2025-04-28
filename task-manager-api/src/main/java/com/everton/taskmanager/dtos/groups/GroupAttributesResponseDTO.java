package com.everton.taskmanager.dtos.groups;

import com.everton.taskmanager.dtos.attribute.AttributeResponseDTO;

import java.util.List;

public record GroupAttributesResponseDTO(
        List<AttributeResponseDTO> taskTypes,
        List<AttributeResponseDTO> eventTypes,
        List<AttributeResponseDTO> taskStatuses
) {
}
