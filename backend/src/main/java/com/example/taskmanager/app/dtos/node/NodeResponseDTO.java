package com.example.taskmanager.app.dtos.node;

import com.example.taskmanager.app.dtos.attributes.TaskStatusResponseDTO;
import com.example.taskmanager.app.dtos.attributes.TaskTypeResponseDTO;
import com.example.taskmanager.app.entities.NodeKindEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NodeResponseDTO(
        UUID id,
        Long rank,
        NodeKindEnum kind,
        String name,
        UUID parentNodeId,
        UUID userId,
        List<NodeResponseDTO> children,
        List<TaskTypeResponseDTO> taskTypes,
        List<TaskStatusResponseDTO> taskStatuses,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
