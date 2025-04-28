package com.everton.taskmanager.dtos.groups;

import com.everton.taskmanager.dtos.attribute.SaveAttributeDTO;

import java.util.List;

public record SaveAllGroupAttributesDTO(
        List<SaveAttributeDTO> attributes
) {
}
