package com.example.taskmanager.app.dtos.attributes;

import com.example.taskmanager.app.entities.ColorTypesEnum;

public record UpdateTaskAttributeRequestDTO(
        String name,
        ColorTypesEnum color,
        Long orderIndex
) {
}
