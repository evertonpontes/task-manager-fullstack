package com.example.taskmanager.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank(message = "email is required.")
        @Email(message = "must be a valid email address.")
        String email,
        @NotBlank(message = "password is required.")
        String password
) { }
