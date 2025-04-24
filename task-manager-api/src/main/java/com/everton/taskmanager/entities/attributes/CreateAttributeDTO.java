package com.everton.taskmanager.entities.attributes;

import jakarta.validation.constraints.NotBlank;

public record CreateAttributeDTO(

        @NotBlank
        String name,

        @NotBlank
        String color
) {
}
