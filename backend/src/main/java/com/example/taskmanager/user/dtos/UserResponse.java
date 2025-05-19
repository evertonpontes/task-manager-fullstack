package com.example.taskmanager.user.dtos;

import com.example.taskmanager.user.entities.UserRolesEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        UserRolesEnum role,
        String email,
        String picture,
        Boolean isEmailVerified,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
