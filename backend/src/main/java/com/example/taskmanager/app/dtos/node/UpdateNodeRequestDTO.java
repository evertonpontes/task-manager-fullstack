package com.example.taskmanager.app.dtos.node;

import java.util.UUID;

public record UpdateNodeRequestDTO (
        String name,
        Long rank,
        UUID parentNodeId
) {
}
