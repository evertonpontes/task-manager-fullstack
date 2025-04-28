package com.everton.taskmanager.dtos.groups;

import jakarta.validation.constraints.NotBlank;

public record SaveGroupDTO(
        @NotBlank
        String name
) {
}
