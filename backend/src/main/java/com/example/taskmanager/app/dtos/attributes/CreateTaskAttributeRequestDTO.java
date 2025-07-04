package com.example.taskmanager.app.dtos.attributes;

import com.example.taskmanager.app.entities.ColorTypesEnum;
import com.example.taskmanager.utils.validators.ValidColor;
import jakarta.validation.constraints.NotBlank;

public record CreateTaskAttributeRequestDTO(
        @NotBlank(message = "name attribute is required.")
        String name,
        @ValidColor(enumClass = ColorTypesEnum.class, message = "color must be one of the following values: red, orange, yellow, lime, green, blue, indigo, violet, purple, rose")
        String color
) {
}
