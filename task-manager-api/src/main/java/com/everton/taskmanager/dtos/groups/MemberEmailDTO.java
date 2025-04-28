package com.everton.taskmanager.dtos.groups;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberEmailDTO(

        @NotBlank
        @Email
        String email
) {
}
