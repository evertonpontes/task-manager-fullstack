package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.project.ProjectResponseDTO;
import com.example.taskmanager.app.entities.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponseDTO projectToProjectResponseDTO(Project project);
}
