package com.everton.taskmanager.dtos.attribute;

import com.everton.taskmanager.entities.attributes.AttributeTypeEnum;
import jakarta.validation.constraints.NotBlank;

public record SaveAttributeDTO(
        @NotBlank
        String name,

        @NotBlank
        String color,

        @NotBlank
        AttributeTypeEnum type
) {
}
