package com.example.taskmanager.user.dtos;

import com.example.taskmanager.utils.validators.FieldsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldsMatch(fieldName = "password", fieldMatchName = "confirmPassword", message = "passwords do not match.")
public record CreateUserRequest (
        @NotBlank(message = "first name is required.")
        String firstName,
        @NotBlank(message = "last name is required.")
        String lastName,
        @NotBlank(message = "email is required.")
        @Email(message = "must be a valid email address.")
        String email,
        @Size(min = 8, message = "password must be at least 8 length.")
        String password,
        String confirmPassword
) {}
