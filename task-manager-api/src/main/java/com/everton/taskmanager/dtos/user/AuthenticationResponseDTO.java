package com.everton.taskmanager.dtos.user;

import lombok.Builder;

@Builder
public record AuthenticationResponseDTO(
        String token,
        UserResponseDTO user
) {
}
