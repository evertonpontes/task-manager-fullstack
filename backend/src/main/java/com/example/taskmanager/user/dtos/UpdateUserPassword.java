package com.example.taskmanager.user.dtos;

import com.example.taskmanager.utils.validators.FieldsMatch;
import jakarta.validation.constraints.NotBlank;

@FieldsMatch(fieldName = "newPassword", fieldMatchName = "confirmPassword", message = "passwords do not match.")
public record UpdateUserPassword(
        @NotBlank(message = "current password is required.")
        String currentPassword,
        @NotBlank(message = "new password is required.")
        String newPassword,
        String confirmPassword
) {
}
