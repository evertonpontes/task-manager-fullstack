package com.everton.taskmanager.entities.teams;

import jakarta.validation.constraints.NotBlank;

public record SaveTeamDTO (

        @NotBlank
        String name
) {
}
