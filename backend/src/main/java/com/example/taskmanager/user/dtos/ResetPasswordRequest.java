package com.example.taskmanager.user.dtos;

import com.example.taskmanager.utils.validators.FieldsMatch;
import jakarta.validation.constraints.Size;

@FieldsMatch(fieldName = "password", fieldMatchName = "confirmPassword", message = "passwords do not match.")
public record ResetPasswordRequest(
        @Size(min = 8, message = "password must be at least 8 length.")
        String password,
        String confirmPassword
) {
}
