package com.everton.taskmanager.entities.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrgToCreateDTO(

        @NotBlank
        String name
) {
}
