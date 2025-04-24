package com.everton.taskmanager.entities.projects;

import java.util.List;

public record FolderDTO(

        String id,
        String name,
        List<ProjectDTO> projects
) {
}
