package com.example.taskmanager.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "first name is required.")
        String firstName,
        @NotBlank(message = "last name is required.")
        String lastName
) {
}
