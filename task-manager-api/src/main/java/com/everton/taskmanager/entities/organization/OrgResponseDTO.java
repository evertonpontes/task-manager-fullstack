package com.everton.taskmanager.entities.organization;

import lombok.Builder;

@Builder
public record OrgResponseDTO (
        String name,
        String owner_email
) {
}
