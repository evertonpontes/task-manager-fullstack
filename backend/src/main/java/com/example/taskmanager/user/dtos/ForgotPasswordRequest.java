package com.example.taskmanager.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "email is required.")
        @Email(message = "must be a valid email address.")
        String email
) {
}
