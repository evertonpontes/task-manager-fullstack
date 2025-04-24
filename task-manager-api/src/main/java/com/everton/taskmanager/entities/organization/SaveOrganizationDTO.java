package com.everton.taskmanager.entities.organization;

import jakarta.validation.constraints.NotBlank;

public record SaveOrganizationDTO(

        @NotBlank
        String name
) {
}
