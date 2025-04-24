package com.everton.taskmanager.entities.organization;

import com.everton.taskmanager.entities.attributes.AttributeDTO;
import com.everton.taskmanager.entities.projects.FolderDTO;
import com.everton.taskmanager.entities.teams.TeamDTO;

import java.util.List;

public record OrganizationDTO(

        String id,
        String name,
        List<AttributeDTO> taskTypes,
        List<AttributeDTO> eventTypes,
        List<AttributeDTO> taskStatuses,
        List<TeamDTO> teams,
        List<FolderDTO> folders
) {
}
