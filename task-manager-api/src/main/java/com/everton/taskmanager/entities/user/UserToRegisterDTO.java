package com.everton.taskmanager.entities.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserToRegisterDTO(
    @NotBlank
    String name,

    @NotBlank
    @Email
    String email,

    @Size(min = 8, max = 255)
    String password
) {
}
